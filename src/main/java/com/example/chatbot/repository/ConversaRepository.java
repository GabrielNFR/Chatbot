package com.example.chatbot.repository;

import com.example.chatbot.dto.ConversaResumo;
import com.example.chatbot.model.Conversa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConversaRepository extends JpaRepository<Conversa, Long> {
    @Query(value = """
            select new com.example.chatbot.dto.ConversaResumo(
                c.id,
                new com.example.chatbot.dto.ClienteResumo(cl.id, cl.nome),
                c.canal, c.status, c.iniciadaEm, c.ultimaInteracaoEm)
            from Conversa c
            join c.cliente cl
            """,
            countQuery = "select count(c) from Conversa c")
    Page<ConversaResumo> listarResumos(Pageable pageable);
}