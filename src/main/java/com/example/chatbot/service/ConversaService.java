package com.example.chatbot.service;

import com.example.chatbot.dto.ConversaResumo;
import com.example.chatbot.dto.PaginaResponse;
import com.example.chatbot.model.Conversa;
import com.example.chatbot.repository.ConversaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConversaService {

    private final ConversaRepository conversas;

    @Transactional(readOnly = true)
    public PaginaResponse<ConversaResumo> listar(Pageable pageable) {
        Page<Conversa> pagina = conversas.findAll(pageable);
        return PaginaResponse.de(pagina, ConversaResumo::de);
    }
}