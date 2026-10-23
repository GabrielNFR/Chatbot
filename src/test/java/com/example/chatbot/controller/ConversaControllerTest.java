package com.example.chatbot.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ConversaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/conversas devolve o contrato de paginacao")
    void deveRetornarContratoDePaginacao() throws Exception {
        mockMvc.perform(get("/api/conversas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo").isArray())
                .andExpect(jsonPath("$.pagina").value(0))
                .andExpect(jsonPath("$.tamanho").value(20))
                .andExpect(jsonPath("$.totalElementos").exists())
                .andExpect(jsonPath("$.totalPaginas").exists());
    }

    @Test
    @DisplayName("Respeita o parametro size da requisicao")
    void deveRespeitarTamanhoDaPagina() throws Exception {
        mockMvc.perform(get("/api/conversas").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tamanho").value(5));
    }
}
