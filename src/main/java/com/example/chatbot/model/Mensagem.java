package com.example.chatbot.model;

import com.example.chatbot.model.enums.RemetenteMensagem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_mensagens")
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversa_id", nullable = false, updatable = false)
    private Conversa conversa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, updatable = false)
    private RemetenteMensagem remetente;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime enviadaEm;

    protected Mensagem() {
    }

    Mensagem(Conversa conversa, RemetenteMensagem remetente, String conteudo) {
        if (conversa == null) {
            throw new IllegalArgumentException("A mensagem precisa estar vinculada a uma conversa.");
        }
        if (remetente == null) {
            throw new IllegalArgumentException("O remetente da mensagem e obrigatorio.");
        }
        if (conteudo == null || conteudo.isBlank()) {
            throw new IllegalArgumentException("O conteudo da mensagem e obrigatorio.");
        }
        this.conversa = conversa;
        this.remetente = remetente;
        this.conteudo = conteudo.trim();
        this.enviadaEm = LocalDateTime.now();
    }

    public boolean foiEnviadaPeloCliente() {
        return remetente == RemetenteMensagem.CLIENTE;
    }

    public boolean foiEnviadaPeloAssistente() {
        return remetente == RemetenteMensagem.CHATBOT;
    }

    public Long getId() {
        return id;
    }

    public Conversa getConversa() {
        return conversa;
    }

    public RemetenteMensagem getRemetente() {
        return remetente;
    }

    public String getConteudo() {
        return conteudo;
    }

    public LocalDateTime getEnviadaEm() {
        return enviadaEm;
    }
}
