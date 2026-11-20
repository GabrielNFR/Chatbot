package com.example.chatbot.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MensagemAtendenteRequest(
        @NotNull @Positive Long atendenteId,
        @NotBlank @Size(max = 10000) String conteudo
) {
}