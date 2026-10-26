package com.example.chatbot.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.chatbot.model.Conversa;
import com.example.chatbot.model.Mensagem;
import com.example.chatbot.model.enums.RemetenteMensagem;
import com.example.chatbot.model.enums.StatusConversa;
import com.example.chatbot.repository.ClienteRepository;
import com.example.chatbot.repository.ConversaRepository;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class DevDataSeederTest {

    @Autowired
    private ClienteRepository clientes;

    @Autowired
    private ConversaRepository conversas;

    @Test
    @DisplayName("deve criar clientes e conversas quando o banco esta vazio")
    void deveCriarClientesEConversas() {
        assertThat(clientes.count()).isEqualTo(4);
        assertThat(conversas.count()).isEqualTo(4);
    }

    @Test
    @DisplayName("deve semear conversas em status diferentes para exercitar a fila")
    void deveSemearStatusVariados() {
        List<StatusConversa> status = conversas.findAll().stream()
                .map(Conversa::getStatus)
                .toList();

        assertThat(status).containsExactlyInAnyOrder(
                StatusConversa.ATIVA,
                StatusConversa.AGUARDANDO_ATENDIMENTO_HUMANO,
                StatusConversa.ENCERRADA,
                StatusConversa.ATIVA);
    }

    @Test
    @DisplayName("deve semear conversa do canal WhatsApp")
    void deveSemearCanalWhatsApp() {
        List<String> canais = conversas.findAll().stream()
                .map(Conversa::getCanal)
                .toList();

        assertThat(canais).contains("WEB", "WHATSAPP", "APP");
    }

    @Test
    @DisplayName("deve preservar acentos no conteudo das mensagens")
    void devePreservarAcentos() {
        List<String> conteudos = conversas.findAll().stream()
                .flatMap(conversa -> conversa.getMensagens().stream())
                .map(Mensagem::getConteudo)
                .toList();

        assertThat(conteudos).isNotEmpty();
        assertThat(conteudos).anyMatch(conteudo -> conteudo.contains("Olá"));
        assertThat(conteudos).anyMatch(conteudo -> conteudo.contains("até"));
    }

    @Test
    @DisplayName("deve registrar mensagens de cliente, chatbot e sistema")
    void deveSemearRemetentesVariados() {
        List<RemetenteMensagem> remetentes = conversas.findAll().stream()
                .flatMap(conversa -> conversa.getMensagens().stream())
                .map(Mensagem::getRemetente)
                .toList();

        assertThat(remetentes).contains(
                RemetenteMensagem.CLIENTE,
                RemetenteMensagem.CHATBOT,
                RemetenteMensagem.SISTEMA);
    }
}
