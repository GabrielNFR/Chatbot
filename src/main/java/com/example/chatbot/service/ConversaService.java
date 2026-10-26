package com.example.chatbot.service;

import com.example.chatbot.dto.ConversaResumo;
import com.example.chatbot.dto.MensagemResumo;
import com.example.chatbot.dto.PaginaResponse;
import com.example.chatbot.model.Cliente;
import com.example.chatbot.model.Conversa;
import com.example.chatbot.model.Mensagem;
import com.example.chatbot.model.enums.RemetenteMensagem;
import com.example.chatbot.repository.ClienteRepository;
import com.example.chatbot.repository.ConversaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversaService {

    private final ConversaRepository conversas;
    private final ClienteRepository clientes;

    @Transactional(readOnly = true)
    public PaginaResponse<ConversaResumo> listar(Pageable pageable) {
        Page<Conversa> pagina = conversas.findAll(pageable);
        return PaginaResponse.de(pagina, ConversaResumo::de);
    }

    @Transactional(readOnly = true)
    public List<MensagemResumo> listarMensagens(Long conversaId) {
        return buscar(conversaId).getMensagens().stream()
                .map(MensagemResumo::de)
                .toList();
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
            return MensagemResumo.de(mensagem);
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
        Conversa conversa = conversas.save(cliente.iniciarConversa(canalFinal));

        return ConversaResumo.de(conversa);
    }

    private Conversa buscar(Long conversaId) {
        return conversas.findById(conversaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Conversa nao encontrada: " + conversaId));
    }
}