package com.example.chatbot.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;

import com.example.chatbot.model.Cliente;

@DataJpaTest
@TestPropertySource(properties = "spring.jpa.properties.hibernate.generate_statistics=true")
class ListagemConsultasTest {

    private static final int TAMANHO_DA_PAGINA = 20;

    @Autowired
    private ConversaRepository conversas;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    @DisplayName("uma pagina da fila executa apenas duas consultas")
    void paginaDaFilaExecutaDuasConsultas() {
        for (int i = 0; i < TAMANHO_DA_PAGINA; i++) {
            Cliente cliente = new Cliente(
                    "Cliente " + i,
                    String.format("%011d", i + 1),
                    "cliente" + i + "@exemplo.com",
                    null);
            cliente.iniciarConversa("WEB");
            em.persist(cliente);
        }
        em.flush();

        Statistics estatisticas = entityManagerFactory
                .unwrap(SessionFactory.class)
                .getStatistics();
        em.clear();
        estatisticas.clear();

        conversas.listarResumos(PageRequest.of(
                0, TAMANHO_DA_PAGINA,
                Sort.by(Sort.Direction.DESC, "ultimaInteracaoEm", "id")));

        assertEquals(2, estatisticas.getPrepareStatementCount(),
                "esperado uma consulta de conteudo e uma de contagem; "
                        + "qualquer numero acima disso indica entidade sendo hidratada");
    }
}
