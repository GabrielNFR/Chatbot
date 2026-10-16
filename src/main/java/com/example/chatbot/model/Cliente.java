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
        name = "tb_clientes",
        uniqueConstraints = @UniqueConstraint(name = "uk_clientes_cpf", columnNames = "cpf"))
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 11)
    private String cpf;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Conversa> conversas = new ArrayList<>();

    protected Cliente() {
    }

    public Cliente(String nome, String cpf, String email, String telefone) {
        alterarNome(nome);
        alterarCpf(cpf);
        alterarEmail(email);
        alterarTelefone(telefone);
        this.criadoEm = LocalDateTime.now();
    }

    public void alterarNome(String novoNome) {
        if (novoNome == null || novoNome.isBlank()) {
            throw new IllegalArgumentException("O nome do cliente e obrigatorio.");
        }
        this.nome = novoNome.trim();
    }

    public void alterarCpf(String novoCpf) {
        String digitos = somenteDigitos(novoCpf);
        if (digitos.length() != 11) {
            throw new IllegalArgumentException("O CPF deve conter 11 digitos.");
        }
        this.cpf = digitos;
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

    public String getPrimeiroNome() {
        int posicaoEspaco = nome.indexOf(' ');
        return posicaoEspaco < 0 ? nome : nome.substring(0, posicaoEspaco);
    }

    public String getCpfMascarado() {
        return "***.***.***-" + cpf.substring(9);
    }

    public Conversa iniciarConversa(String canal) {
        return iniciarConversa(null, canal);
    }

    public Conversa iniciarConversa(Chatbot chatbot, String canal) {
        Conversa conversa = new Conversa(this, chatbot, canal);
        this.conversas.add(conversa);
        return conversa;
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

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    private static String somenteDigitos(String valor) {
        return valor == null ? "" : valor.replaceAll("\\D", "");
    }
}
