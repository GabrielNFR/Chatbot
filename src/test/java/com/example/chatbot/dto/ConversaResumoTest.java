package com.example.chatbot.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import com.example.chatbot.model.Cliente;
import com.example.chatbot.model.Conversa;

@DataJpaTest
class ConversaResumoTest {

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("clientes homonimos aparecem na listagem distinguidos pelo id")
    void deveDistinguirClientesHomonimosPeloId() {
        Cliente primeiro = new Cliente(
                "Mariana Souza", "111.222.333-44", "mariana.um@exemplo.com", null);
        Conversa conversaDoPrimeiro = primeiro.iniciarConversa("WEB");
        em.persistAndFlush(primeiro);

        Cliente segundo = new Cliente(
                "Mariana Souza", "555.666.777-88", "mariana.dois@exemplo.com", null);
        Conversa conversaDoSegundo = segundo.iniciarConversa("WEB");
        em.persistAndFlush(segundo);

        ConversaResumo resumoDoPrimeiro = ConversaResumo.de(conversaDoPrimeiro);
        ConversaResumo resumoDoSegundo = ConversaResumo.de(conversaDoSegundo);

        // Mesmo nome: o texto nao serve para diferenciar.
        assertEquals("Mariana Souza", resumoDoPrimeiro.cliente().nome());
        assertEquals("Mariana Souza", resumoDoSegundo.cliente().nome());

        // O id e o que distingue as duas pessoas.
        assertNotNull(resumoDoPrimeiro.cliente().id());
        assertNotNull(resumoDoSegundo.cliente().id());
        assertNotEquals(resumoDoPrimeiro.cliente().id(), resumoDoSegundo.cliente().id());

        assertEquals(primeiro.getId(), resumoDoPrimeiro.cliente().id());
        assertEquals(segundo.getId(), resumoDoSegundo.cliente().id());
    }

    @Test
    @DisplayName("conversas diferentes apontam para clientes diferentes")
    void deveManterOClienteDeCadaConversa() {
        Cliente mariana = new Cliente(
                "Mariana Souza", "111.222.333-44", "mariana.um@exemplo.com", null);
        Conversa primeiraConversa = mariana.iniciarConversa("WEB");
        em.persistAndFlush(mariana);

        Cliente carlos = new Cliente(
                "Carlos Pereira", "555.666.777-88", "carlos@exemplo.com", null);
        Conversa segundaConversa = carlos.iniciarConversa("WHATSAPP");
        em.persistAndFlush(carlos);

        assertEquals("Mariana Souza", ConversaResumo.de(primeiraConversa).cliente().nome());
        assertEquals("Carlos Pereira", ConversaResumo.de(segundaConversa).cliente().nome());
    }
}
