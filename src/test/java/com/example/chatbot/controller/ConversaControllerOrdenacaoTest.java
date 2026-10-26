package com.example.chatbot.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.chatbot.dto.ConversaResumo;
import com.example.chatbot.dto.PaginaResponse;
import com.example.chatbot.service.ConversaService;

@SpringBootTest
@AutoConfigureMockMvc
class ConversaControllerOrdenacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConversaService service;

    @Test
    @DisplayName("a ordenacao padrao desempata por id")
    void deveDesempatarPorId() throws Exception {
        when(service.listar(any(Pageable.class)))
                .thenReturn(new PaginaResponse<ConversaResumo>(List.of(), 0, 20, 0, 0));

        mockMvc.perform(get("/api/conversas")).andExpect(status().isOk());

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(service).listar(captor.capture());

        List<String> ordenacao = new ArrayList<>();
        captor.getValue().getSort().forEach(ordem ->
                ordenacao.add(ordem.getProperty() + ":" + ordem.getDirection()));

        assertThat(ordenacao).containsExactly("ultimaInteracaoEm:DESC", "id:DESC");
    }
}
