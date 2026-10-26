package com.example.chatbot.config;

import com.example.chatbot.model.Cliente;
import com.example.chatbot.model.Conversa;
import com.example.chatbot.model.enums.RemetenteMensagem;
import com.example.chatbot.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Cria dados de exemplo para desenvolvimento.
 */
@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevDataSeeder implements ApplicationRunner {

    private final ClienteRepository clientes;

    @Override
    public void run(ApplicationArguments args) {
        if (clientes.count() > 0) {
            log.info("Seeder ignorado: o banco ja possui clientes cadastrados.");
            return;
        }

        List<Cliente> semeados = List.of(
                marianaComConversaAtiva(),
                rafaelAguardandoAtendimentoHumano(),
                camilaComConversaEncerrada(),
                brunoComConversaSemMensagens());

        clientes.saveAll(semeados);
        log.info("Seeder criou {} clientes com conversas de exemplo.", semeados.size());
    }

    /** Conversa ativa com histórico completo: o caso mais comum da fila. */
    private Cliente marianaComConversaAtiva() {
        Cliente mariana = new Cliente(
                "Mariana Souza", "12345678901", "mariana.souza@exemplo.com", "(11) 99999-8888");

        Conversa conversa = mariana.iniciarConversa("WEB");
        conversa.registrarMensagem(RemetenteMensagem.CLIENTE,
                "Oi, preciso de ajuda com o meu pedido.");
        conversa.registrarMensagem(RemetenteMensagem.CHATBOT,
                "Olá, Mariana! Claro, pode me informar o número do pedido?");
        conversa.registrarMensagem(RemetenteMensagem.CLIENTE, "É o pedido 4821.");
        conversa.registrarMensagem(RemetenteMensagem.CHATBOT,
                "O pedido 4821 saiu para entrega e chega em até 2 dias úteis.");

        return mariana;
    }

    /** Caso que o atendente precisa ver: cliente pediu transferência. */
    private Cliente rafaelAguardandoAtendimentoHumano() {
        Cliente rafael = new Cliente(
                "Rafael Lima", "98765432100", "rafael.lima@exemplo.com", "(21) 98888-7777");

        Conversa conversa = rafael.iniciarConversa("WHATSAPP");
        conversa.registrarMensagem(RemetenteMensagem.CLIENTE,
                "O chatbot não resolveu, quero falar com uma pessoa.");
        conversa.registrarMensagem(RemetenteMensagem.SISTEMA,
                "Conversa encaminhada para a fila de atendimento humano.");
        conversa.transferirParaAtendimentoHumano();

        return rafael;
    }

    /** Conversa encerrada: exercita o filtro de status e a ordenação. */
    private Cliente camilaComConversaEncerrada() {
        Cliente camila = new Cliente(
                "Camila Ferreira", "11122233344", "camila.ferreira@exemplo.com", null);

        Conversa conversa = camila.iniciarConversa("WEB");
        conversa.registrarMensagem(RemetenteMensagem.CLIENTE,
                "Qual é o horário de funcionamento?");
        conversa.registrarMensagem(RemetenteMensagem.CHATBOT,
                "Atendemos de segunda a sexta, das 8h às 18h.");
        conversa.encerrar();

        return camila;
    }

    /** Conversa recém-aberta: exercita o estado de histórico vazio no front. */
    private Cliente brunoComConversaSemMensagens() {
        Cliente bruno = new Cliente(
                "Bruno Alves", "55566677788", "bruno.alves@exemplo.com", "(31) 97777-6666");

        bruno.iniciarConversa("APP");

        return bruno;
    }
}
