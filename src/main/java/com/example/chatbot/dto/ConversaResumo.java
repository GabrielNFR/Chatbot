package com.example.chatbot.dto;

import com.example.chatbot.model.Conversa;
import com.example.chatbot.model.enums.StatusConversa;

import java.time.LocalDateTime;

public record ConversaResumo(
        Long id,
        String cliente,
        String canal,
        StatusConversa status,
        LocalDateTime iniciadaEm,
        LocalDateTime ultimaInteracaoEm) {

    public static ConversaResumo de(Conversa conversa) {
        return new ConversaResumo(
                conversa.getId(),
                conversa.getCliente().getNome(),
                conversa.getCanal(),
                conversa.getStatus(),
                conversa.getIniciadaEm(),
                conversa.getUltimaInteracaoEm());
    }
}