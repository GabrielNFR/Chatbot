package com.example.chatbot.model;

import com.example.chatbot.model.enums.StatusAtendimento;
import com.example.chatbot.model.enums.StatusConversa;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AtendimentoTest {

    @Test
    void chatbotIniciaConversaEAtendenteAssumeAtendimento() {
        Cliente cliente = new Cliente("Ana Silva", "123.456.789-00", "ana@example.com", null);
        Chatbot chatbot = new Chatbot("Assistente", "Ola!");
        Atendente atendente = new Atendente("Joao", "joao@example.com", null);

        Conversa conversa = chatbot.iniciarConversa(cliente, "web");
        conversa.transferirParaAtendimentoHumano();
        Atendimento atendimento = atendente.assumir(conversa);

        assertEquals(chatbot, conversa.getChatbot());
        assertEquals(conversa, cliente.getConversas().get(0));
        assertEquals(conversa, chatbot.getConversas().get(0));
        assertEquals(atendente, atendimento.getAtendente());
        assertEquals(conversa, atendimento.getConversa());
        assertEquals(StatusAtendimento.EM_ANDAMENTO, atendimento.getStatus());
        assertEquals(StatusConversa.EM_ATENDIMENTO_HUMANO, conversa.getStatus());
        assertNotNull(atendimento.getAbertoEm());
    }

    @Test
    void naoPermiteAtendenteInativoAssumirConversa() {
        Cliente cliente = new Cliente("Ana Silva", "123.456.789-00", "ana@example.com", null);
        Conversa conversa = cliente.iniciarConversa("web");
        conversa.transferirParaAtendimentoHumano();
        Atendente atendente = new Atendente("Joao", "joao@example.com", null);
        atendente.desativar();

        assertThrows(IllegalArgumentException.class, () -> atendente.assumir(conversa));
        assertEquals(StatusConversa.AGUARDANDO_ATENDIMENTO_HUMANO, conversa.getStatus());
    }

    @Test
    void encerrarAtendimentoTambemEncerraConversa() {
        Cliente cliente = new Cliente("Ana Silva", "123.456.789-00", "ana@example.com", null);
        Conversa conversa = cliente.iniciarConversa("web");
        conversa.transferirParaAtendimentoHumano();
        Atendimento atendimento = new Atendente("Joao", "joao@example.com", null).assumir(conversa);

        atendimento.encerrar();

        assertEquals(StatusAtendimento.ENCERRADO, atendimento.getStatus());
        assertEquals(StatusConversa.ENCERRADA, conversa.getStatus());
        assertNotNull(atendimento.getEncerradoEm());
    }

    @Test
    void naoPermiteChatbotDesativadoIniciarConversa() {
        Chatbot chatbot = new Chatbot("Assistente", null);
        chatbot.desativar();
        Cliente cliente = new Cliente("Ana Silva", "123.456.789-00", "ana@example.com", null);

        assertThrows(IllegalStateException.class, () -> chatbot.iniciarConversa(cliente, "web"));
    }
}
