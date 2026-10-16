package com.example.chatbot.model;

import com.example.chatbot.model.enums.StatusAtendimento;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_atendimentos")
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversa_id", nullable = false, unique = true, updatable = false)
    private Conversa conversa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "atendente_id", nullable = false, updatable = false)
    private Atendente atendente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAtendimento status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime abertoEm;

    private LocalDateTime resolvidoEm;

    private LocalDateTime encerradoEm;

    protected Atendimento() {
    }

    Atendimento(Conversa conversa, Atendente atendente) {
        if (conversa == null) {
            throw new IllegalArgumentException("O atendimento precisa estar vinculado a uma conversa.");
        }
        if (atendente == null || !atendente.estaAtivo()) {
            throw new IllegalArgumentException("O atendimento precisa de um atendente ativo.");
        }
        this.conversa = conversa;
        this.atendente = atendente;
        this.status = StatusAtendimento.EM_ANDAMENTO;
        this.abertoEm = LocalDateTime.now();
    }

    public void resolver() {
        if (status != StatusAtendimento.EM_ANDAMENTO) {
            throw new IllegalStateException("Somente um atendimento em andamento pode ser resolvido.");
        }
        this.status = StatusAtendimento.RESOLVIDO;
        this.resolvidoEm = LocalDateTime.now();
    }

    public void encerrar() {
        if (encerrado()) {
            throw new IllegalStateException("O atendimento ja foi encerrado.");
        }
        encerrarInternamente();
        conversa.encerrar();
    }

    void encerrarPorConversa() {
        if (!encerrado()) {
            encerrarInternamente();
        }
    }

    private void encerrarInternamente() {
        this.status = StatusAtendimento.ENCERRADO;
        this.encerradoEm = LocalDateTime.now();
    }

    public boolean encerrado() {
        return status == StatusAtendimento.ENCERRADO;
    }

    public Long getId() {
        return id;
    }

    public Conversa getConversa() {
        return conversa;
    }

    public Atendente getAtendente() {
        return atendente;
    }

    public StatusAtendimento getStatus() {
        return status;
    }

    public LocalDateTime getAbertoEm() {
        return abertoEm;
    }

    public LocalDateTime getResolvidoEm() {
        return resolvidoEm;
    }

    public LocalDateTime getEncerradoEm() {
        return encerradoEm;
    }
}
