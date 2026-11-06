package com.example.chatbot.service;

import com.example.chatbot.dto.ConversaResumo;
import com.example.chatbot.dto.MensagemResumo;
import com.example.chatbot.dto.PaginaResponse;
import com.example.chatbot.model.Cliente;
import com.example.chatbot.model.Chatbot;
import com.example.chatbot.model.enums.StatusConversa;
import com.example.chatbot.repository.ChatbotRepository;
import org.springframework.beans.factory.annotation.Value;
import com.example.chatbot.model.Conversa;
import com.example.chatbot.model.Mensagem;
import com.example.chatbot.model.enums.RemetenteMensagem;
import com.example.chatbot.repository.ClienteRepository;
import com.example.chatbot.repository.ConversaRepository;
import com.example.chatbot.repository.MensagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ConversaService {

    private static final int TAMANHO_MAXIMO_DA_PAGINA = 100;

    private final ConversaRepository conversas;
    private final ClienteRepository clientes;
    private final ChatbotRepository chatbots;

    @Value("${app.atendimento.horario:de segunda a sexta, das 8h às 18h}")
    private String horarioAtendimento;
    private final MensagemRepository mensagens;

    @Transactional(readOnly = true)
    public PaginaResponse<ConversaResumo> listar(Pageable pageable) {
        Page<ConversaResumo> pagina = conversas.listarResumos(pageable);
        return PaginaResponse.de(pagina, Function.identity());
    }

    @Transactional(readOnly = true)
    public PaginaResponse<MensagemResumo> listarMensagens(Long conversaId, Pageable pageable) {
        Pageable consulta = PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), TAMANHO_MAXIMO_DA_PAGINA),
                Sort.by(Sort.Order.desc("enviadaEm"), Sort.Order.desc("id")));

        Page<MensagemResumo> pagina = mensagens.listarResumosPorConversa(conversaId, consulta);

        if (pagina.getTotalElements() == 0 && !conversas.existsById(conversaId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Conversa nao encontrada: " + conversaId);
        }

        List<MensagemResumo> conteudo = new ArrayList<>(pagina.getContent());
        Collections.reverse(conteudo);

        return new PaginaResponse<>(
                conteudo,
                pagina.getNumber(),
                pagina.getSize(),
                pagina.getTotalElements(),
                pagina.getTotalPages());
    }

    @Transactional
    public MensagemResumo registrarMensagem(Long conversaId, String conteudo) {
        if (conteudo == null || conteudo.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "O conteudo da mensagem e obrigatorio.");
        }

        Conversa conversa = buscar(conversaId);

        try {
            Mensagem mensagem = conversa.registrarMensagem(RemetenteMensagem.CLIENTE, conteudo);
            return MensagemResumo.de(mensagens.save(mensagem));
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @Transactional
    public ConversaResumo criarConversa(Long clienteId, String canal) {
        if (clienteId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "O clienteId e obrigatorio.");
        }

        Cliente cliente = clientes.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente nao encontrado: " + clienteId));

        String canalFinal = (canal == null || canal.isBlank()) ? "WEB" : canal;
        Chatbot chatbot = chatbots.findFirstByAtivoTrueOrderByIdAsc()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Nenhum chatbot ativo."));
        Conversa conversa = conversas.save(chatbot.iniciarConversa(cliente, canalFinal));

        return ConversaResumo.de(conversa);
    }

    @Transactional
    public ConversaResumo transferirParaAtendente(Long conversaId, String motivo) {
        Conversa conversa = buscar(conversaId);
        try {
            conversa.transferirParaAtendimentoHumano(motivo);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        conversa.registrarMensagem(RemetenteMensagem.SISTEMA,
                "Sua conversa está na fila de atendimento humano. Nosso horário é " + horarioAtendimento + ".");
        return ConversaResumo.de(conversa);
    }

    @Transactional(readOnly = true)
    public List<ConversaResumo> listarFila() {
        return conversas.findByStatusOrderByIniciadaEmAscIdAsc(StatusConversa.AGUARDANDO_ATENDIMENTO_HUMANO)
                .stream().map(ConversaResumo::de).toList();
    }

    private Conversa buscar(Long conversaId) {
        return conversas.buscarParaAtualizar(conversaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Conversa nao encontrada: " + conversaId));
    }
}
