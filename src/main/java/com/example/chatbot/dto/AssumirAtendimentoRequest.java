package com.example.chatbot.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AssumirAtendimentoRequest(
        @NotNull @Positive Long atendenteId
) {
}
