package com.example.chatbot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "tb_chatbots")
public class Chatbot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 500)
    private String mensagemBoasVindas;

    @Column(nullable = false)
    private boolean ativo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "chatbot")
    private List<Conversa> conversas = new ArrayList<>();

    protected Chatbot() {
    }

    public Chatbot(String nome, String mensagemBoasVindas) {
        alterarNome(nome);
        alterarMensagemBoasVindas(mensagemBoasVindas);
        this.ativo = true;
        this.criadoEm = LocalDateTime.now();
    }

    public void alterarNome(String novoNome) {
        if (novoNome == null || novoNome.isBlank()) {
            throw new IllegalArgumentException("O nome do chatbot e obrigatorio.");
        }
        this.nome = novoNome.trim();
    }

    public void alterarMensagemBoasVindas(String mensagem) {
        this.mensagemBoasVindas = mensagem == null || mensagem.isBlank() ? null : mensagem.trim();
    }

    public void desativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }

    public Conversa iniciarConversa(Cliente cliente, String canal) {
        if (!ativo) {
            throw new IllegalStateException("O chatbot esta desativado.");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("A conversa precisa estar vinculada a um cliente.");
        }
        Conversa conversa = cliente.iniciarConversa(this, canal);
        this.conversas.add(conversa);
        return conversa;
    }

    public boolean estaAtivo() {
        return ativo;
    }

    public List<Conversa> getConversas() {
        return Collections.unmodifiableList(conversas);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getMensagemBoasVindas() {
        return mensagemBoasVindas;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
