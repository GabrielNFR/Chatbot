package com.example.chatbot.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(
        name = "tb_atendentes",
        uniqueConstraints = @UniqueConstraint(name = "uk_atendentes_email", columnNames = "email"))
public class Atendente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(nullable = false)
    private boolean ativo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "atendente", cascade = CascadeType.ALL)
    private List<Atendimento> atendimentos = new ArrayList<>();

    protected Atendente() {
    }

    public Atendente(String nome, String email, String telefone) {
        alterarNome(nome);
        alterarEmail(email);
        alterarTelefone(telefone);
        this.ativo = true;
        this.criadoEm = LocalDateTime.now();
    }

    public void alterarNome(String novoNome) {
        if (novoNome == null || novoNome.isBlank()) {
            throw new IllegalArgumentException("O nome do atendente e obrigatorio.");
        }
        this.nome = novoNome.trim();
    }

    public void alterarEmail(String novoEmail) {
        if (novoEmail == null || novoEmail.isBlank() || !novoEmail.contains("@")) {
            throw new IllegalArgumentException("O e-mail informado e invalido.");
        }
        this.email = novoEmail.trim().toLowerCase();
    }

    public void alterarTelefone(String novoTelefone) {
        this.telefone = (novoTelefone == null || novoTelefone.isBlank()) ? null : novoTelefone.trim();
    }

    public void desativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }

    public Atendimento assumir(Conversa conversa) {
        if (conversa == null) {
            throw new IllegalArgumentException("A conversa e obrigatoria.");
        }
        return conversa.abrirAtendimento(this);
    }

    void registrarAtendimento(Atendimento atendimento) {
        this.atendimentos.add(atendimento);
    }

    public boolean estaAtivo() {
        return ativo;
    }

    public List<Atendimento> getAtendimentos() {
        return Collections.unmodifiableList(atendimentos);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
