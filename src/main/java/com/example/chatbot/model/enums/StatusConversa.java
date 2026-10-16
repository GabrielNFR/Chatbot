package com.example.chatbot.model.enums;

public enum StatusConversa {
    ATIVA, AGUARDANDO_ATENDIMENTO_HUMANO, EM_ATENDIMENTO_HUMANO, ENCERRADA;

    public boolean encerrada() {
        return this == ENCERRADA;
    }
}
