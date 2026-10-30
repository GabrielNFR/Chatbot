package com.example.chatbot.controller;

import com.example.chatbot.dto.ConversaResumo;
import com.example.chatbot.dto.MensagemResumo;
import com.example.chatbot.dto.NovaConversaRequest;
import com.example.chatbot.dto.NovaMensagemRequest;
import com.example.chatbot.dto.PaginaResponse;
import com.example.chatbot.service.ConversaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conversas")
@RequiredArgsConstructor
public class ConversaController {

    private final ConversaService service;

    @GetMapping
    public PaginaResponse<ConversaResumo> listar(
            @PageableDefault(size = 20, sort = {"ultimaInteracaoEm", "id"}, direction = Sort.Direction.DESC)
            Pageable pageable) {
        return service.listar(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConversaResumo criar(@RequestBody NovaConversaRequest request) {
        return service.criarConversa(request.clienteId(), request.canal());
    }

    @GetMapping("/{id}/mensagens")
    public PaginaResponse<MensagemResumo> listarMensagens(
            @PathVariable Long id,
            @PageableDefault(size = 50) Pageable pageable) {
        return service.listarMensagens(id, pageable);
    }

    @PostMapping("/{id}/mensagens")
    @ResponseStatus(HttpStatus.CREATED)
    public MensagemResumo registrarMensagem(@PathVariable Long id,
                                            @RequestBody NovaMensagemRequest request) {
        return service.registrarMensagem(id, request.conteudo());
    }
}