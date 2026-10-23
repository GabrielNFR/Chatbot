package com.example.chatbot.controller;

import com.example.chatbot.dto.ConversaResumo;
import com.example.chatbot.dto.PaginaResponse;
import com.example.chatbot.service.ConversaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conversas")
@RequiredArgsConstructor
public class ConversaController {

    private final ConversaService service;

    @GetMapping
    public PaginaResponse<ConversaResumo> listar(
            @PageableDefault(size = 20, sort = "ultimaInteracaoEm", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return service.listar(pageable);
    }
}