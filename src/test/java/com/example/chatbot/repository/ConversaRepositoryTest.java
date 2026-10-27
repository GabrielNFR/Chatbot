package com.example.chatbot.repository;

import com.example.chatbot.dto.ConversaResumo;
import com.example.chatbot.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ConversaRepositoryTest {

    @Autowired
    private ConversaRepository conversas;

    @Autowired
    private TestEntityManager em;

    @BeforeEach
    void setUp() {
        Cliente cliente = new Cliente("Ana Silva", "123.456.789-00", "ana@example.com", null);

        for (int i = 0; i < 3; i++) {
            cliente.iniciarConversa("WEB");
        }

        em.persistAndFlush(cliente);
        em.clear();
    }

    @Test
    @DisplayName("Pagina a listagem respeitando tamanho e total de elementos")
    void devePaginarAsConversas() {
        Page<ConversaResumo> pagina = conversas.listarResumos(
                PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "ultimaInteracaoEm", "id")));

        assertEquals(3, pagina.getTotalElements());
        assertEquals(2, pagina.getContent().size());
        assertEquals(2, pagina.getTotalPages());
    }

    @Test
    @DisplayName("Ordena por ultimaInteracaoEm decrescente")
    void deveOrdenarPorUltimaInteracaoDecrescente() {
        List<ConversaResumo> lista = conversas.listarResumos(
                        PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "ultimaInteracaoEm", "id")))
                .getContent();

        assertEquals(3, lista.size());

        for (int i = 1; i < lista.size(); i++) {
            LocalDateTime anterior = lista.get(i - 1).ultimaInteracaoEm();
            LocalDateTime atual = lista.get(i).ultimaInteracaoEm();
            assertTrue(!anterior.isBefore(atual),
                    "A lista deveria estar em ordem decrescente de ultimaInteracaoEm");
        }
    }

    @Test
    @DisplayName("Traz o cliente de cada conversa ja projetado")
    void deveTrazerOClienteProjetado() {
        ConversaResumo primeiro = conversas.listarResumos(
                        PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "ultimaInteracaoEm", "id")))
                .getContent().get(0);

        assertNotNull(primeiro.cliente().id());
        assertEquals("Ana Silva", primeiro.cliente().nome());
    }
}
