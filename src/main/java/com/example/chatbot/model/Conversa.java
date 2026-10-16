package com.example.chatbot.model;

import com.example.chatbot.model.enums.RemetenteMensagem;
import com.example.chatbot.model.enums.StatusConversa;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "tb_conversas")
public class Conversa {

    private static final Duration TEMPO_MAXIMO_INATIVIDADE = Duration.ofMinutes(30);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false, updatable = false)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusConversa status;

    @Column(nullable = false, length = 30, updatable = false)
    private String canal;

    @Column(nullable = false, updatable = false)
    private LocalDateTime iniciadaEm;

    @Column(nullable = false)
    private LocalDateTime ultimaInteracaoEm;

    private LocalDateTime encerradaEm;

    @OneToMany(mappedBy = "conversa", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("enviadaEm ASC")
    private List<Mensagem> mensagens = new ArrayList<>();

    protected Conversa() {
    }

    Conversa(Cliente cliente, String canal) {
        if (cliente == null) {
            throw new IllegalArgumentException("A conversa precisa estar vinculada a um cliente.");
        }
        if (canal == null || canal.isBlank()) {
            throw new IllegalArgumentException("O canal da conversa e obrigatorio.");
        }
        this.cliente = cliente;
        this.canal = canal.trim().toUpperCase();
        this.status = StatusConversa.ATIVA;
        this.iniciadaEm = LocalDateTime.now();
        this.ultimaInteracaoEm = this.iniciadaEm;
    }

    public Mensagem registrarMensagem(RemetenteMensagem remetente, String conteudo) {
        exigirNaoEncerrada();
        Mensagem mensagem = new Mensagem(this, remetente, conteudo);
        this.mensagens.add(mensagem);
        this.ultimaInteracaoEm = mensagem.getEnviadaEm();
        return mensagem;
    }

    public void transferirParaAtendimentoHumano() {
        exigirNaoEncerrada();
        this.status = StatusConversa.AGUARDANDO_ATENDIMENTO_HUMANO;
        this.ultimaInteracaoEm = LocalDateTime.now();
    }

    public void assumirPorAtendente() {
        if (status != StatusConversa.AGUARDANDO_ATENDIMENTO_HUMANO) {
            throw new IllegalStateException("A conversa nao esta aguardando atendimento humano.");
        }
        this.status = StatusConversa.EM_ATENDIMENTO_HUMANO;
        this.ultimaInteracaoEm = LocalDateTime.now();
    }

    public void encerrar() {
        exigirNaoEncerrada();
        this.status = StatusConversa.ENCERRADA;
        this.encerradaEm = LocalDateTime.now();
    }

    public boolean encerrarSeInativa() {
        if (encerrada() || !inatividadeExcedida()) {
            return false;
        }
        encerrar();
        return true;
    }

    public boolean inatividadeExcedida() {
        return Duration.between(ultimaInteracaoEm, LocalDateTime.now())
                .compareTo(TEMPO_MAXIMO_INATIVIDADE) > 0;
    }

    public boolean estaAtiva() {
        return status == StatusConversa.ATIVA;
    }

    public boolean emAtendimentoHumano() {
        return status == StatusConversa.EM_ATENDIMENTO_HUMANO;
    }

    public boolean encerrada() {
        return status.encerrada();
    }

    private void exigirNaoEncerrada() {
        if (encerrada()) {
            throw new IllegalStateException("A conversa ja foi encerrada.");
        }
    }

    public List<Mensagem> getMensagens() {
        return Collections.unmodifiableList(mensagens);
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public StatusConversa getStatus() {
        return status;
    }

    public String getCanal() {
        return canal;
    }

    public LocalDateTime getIniciadaEm() {
        return iniciadaEm;
    }

    public LocalDateTime getUltimaInteracaoEm() {
        return ultimaInteracaoEm;
    }

    public LocalDateTime getEncerradaEm() {
        return encerradaEm;
    }
}
