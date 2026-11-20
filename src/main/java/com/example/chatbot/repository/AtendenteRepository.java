package com.example.chatbot.repository;

import com.example.chatbot.model.Atendente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtendenteRepository extends JpaRepository<Atendente, Long> {
}
