package com.example.chatbot.dto;

import com.example.chatbot.model.Cliente;

public record ClienteResumo(Long id, String nome) {

    public static ClienteResumo de(Cliente cliente) {
        return new ClienteResumo(cliente.getId(), cliente.getNome());
    }
}
