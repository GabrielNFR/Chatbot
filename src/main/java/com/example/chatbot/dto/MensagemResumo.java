package com.example.chatbot.dto;

import com.example.chatbot.model.Mensagem;
import com.example.chatbot.model.enums.RemetenteMensagem;

import java.time.LocalDateTime;

public record MensagemResumo(
        Long id,
        RemetenteMensagem remetente,
        String conteudo,
        LocalDateTime enviadaEm) {

    public static MensagemResumo de(Mensagem mensagem) {
        return new MensagemResumo(
                mensagem.getId(),
                mensagem.getRemetente(),
                mensagem.getConteudo(),
                mensagem.getEnviadaEm());
    }
}
