package com.example.chatbot.repository;

import com.example.chatbot.model.Conversa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversaRepository extends JpaRepository<Conversa, Long> {

    @Override
    @EntityGraph(attributePaths = "cliente")
    Page<Conversa> findAll(Pageable pageable);
}