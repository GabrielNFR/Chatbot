package com.example.chatbot.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifica a configuração de CORS aplicada aos endpoints {@code /api/**}.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CorsConfigTest {

    private static final String ORIGEM_PERMITIDA = "http://localhost:5173";
    private static final String ORIGEM_NAO_PERMITIDA = "http://origem-nao-autorizada.example.com";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Preflight de origem permitida deve retornar os headers de CORS")
    void preflightDeOrigemPermitidaDeveRetornarHeadersDeCors() throws Exception {
        mockMvc.perform(options("/api/messages")
                        .header("Origin", ORIGEM_PERMITIDA)
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "Content-Type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", ORIGEM_PERMITIDA))
                .andExpect(header().exists("Access-Control-Allow-Methods"))
                .andExpect(header().exists("Access-Control-Allow-Headers"));
    }

    @Test
    @DisplayName("Preflight da origem do pnpm dev (:3000) deve ser permitido")
    void preflightDaOrigemDoPnpmDevDeveSerPermitido() throws Exception {
        String origem = "http://localhost:3000";
        mockMvc.perform(options("/api/messages")
                        .header("Origin", origem)
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", origem));
    }

    @Test
    @DisplayName("Origem não permitida deve ser rejeitada sem header de CORS")
    void preflightDeOrigemNaoPermitidaDeveSerRejeitado() throws Exception {
        mockMvc.perform(options("/api/messages")
                        .header("Origin", ORIGEM_NAO_PERMITIDA)
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }
}
