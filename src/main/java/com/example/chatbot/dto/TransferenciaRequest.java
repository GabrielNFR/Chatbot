package com.example.chatbot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TransferenciaRequest(@NotBlank @Size(max = 500) String motivo) {
}
