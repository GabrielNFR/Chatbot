package com.example.chatbot.repository;

import com.example.chatbot.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCpf(String cpf);
    List<Cliente> findByEmailIgnoreCase(String email);
}
