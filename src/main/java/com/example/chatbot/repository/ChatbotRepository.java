package com.example.chatbot.repository;

import com.example.chatbot.model.Chatbot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatbotRepository extends JpaRepository<Chatbot, Long> {
    Optional<Chatbot> findFirstByAtivoTrueOrderByIdAsc();
}
