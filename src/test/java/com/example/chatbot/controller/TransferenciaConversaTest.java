package com.example.chatbot.controller;

import com.example.chatbot.model.Atendente;
import com.example.chatbot.model.Cliente;
import com.example.chatbot.model.Conversa;
import com.example.chatbot.model.enums.RemetenteMensagem;
import com.example.chatbot.repository.ClienteRepository;
import com.example.chatbot.repository.ConversaRepository;
import com.example.chatbot.repository.MensagemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransferenciaConversaTest {
    @Autowired MockMvc api;
    @Autowired ClienteRepository clientes;
    @Autowired ConversaRepository conversas;
    @Autowired MensagemRepository mensagens;
    @Autowired TransactionTemplate transacao;

    @Test
    void transfereUmaVezEPreservaMensagensDoCliente() throws Exception {
        Long id = transacao.execute(status -> {
            Cliente cliente = clientes.save(new Cliente("Mariana", "00000000017", "m@example.com", null));
            return conversas.save(cliente.iniciarConversa("WEB")).getId();
        });
        String rota = "/api/conversas/" + id;
        String pedido = "{\"motivo\":\"Cliente pediu atendimento humano\"}";
        api.perform(post(rota + "/transferencia").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
        api.perform(post(rota + "/transferencia").contentType(MediaType.APPLICATION_JSON).content(pedido))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AGUARDANDO_ATENDIMENTO_HUMANO"));
        api.perform(get("/api/conversas/fila"))
                .andExpect(jsonPath("$[?(@.id == " + id + ")]").isNotEmpty());
        api.perform(post(rota + "/transferencia").contentType(MediaType.APPLICATION_JSON).content(pedido))
                .andExpect(status().isConflict());
        api.perform(post(rota + "/mensagens").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"conteudo\":\"Ainda estou aqui\"}"))
                .andExpect(status().isCreated());
        var historico = mensagens.findByConversaIdOrderByIdAsc(id);
        assertThat(historico).extracting(m -> m.getRemetente())
                .containsExactly(RemetenteMensagem.SISTEMA, RemetenteMensagem.CLIENTE);
        assertThat(historico.getFirst().getConteudo()).contains("horário");
        transacao.executeWithoutResult(status -> {
            Conversa conversa = conversas.findById(id).orElseThrow();
            assertThat(conversa.getMotivoTransferencia()).isEqualTo("Cliente pediu atendimento humano");
            assertThatThrownBy(() -> conversa.registrarMensagem(RemetenteMensagem.CHATBOT, "Resposta"))
                    .isInstanceOf(IllegalStateException.class);
            new Atendente("Roberto", "r@example.com", null).assumir(conversa);
            assertThatThrownBy(() -> conversa.registrarMensagem(RemetenteMensagem.CHATBOT, "Resposta"))
                    .isInstanceOf(IllegalStateException.class);
            assertThatThrownBy(() -> conversa.transferirParaAtendimentoHumano("Novamente"))
                    .isInstanceOf(IllegalStateException.class);
            status.setRollbackOnly();
        });
    }
}
