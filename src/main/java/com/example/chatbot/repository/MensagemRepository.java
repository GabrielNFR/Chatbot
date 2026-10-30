package com.example.chatbot.repository;

import com.example.chatbot.dto.MensagemResumo;
import com.example.chatbot.model.Mensagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {

    @Query(value = """
            select new com.example.chatbot.dto.MensagemResumo(
                m.id, m.remetente, m.conteudo, m.enviadaEm)
            from Mensagem m
            where m.conversa.id = :conversaId
            """,
            countQuery = """
            select count(m)
            from Mensagem m
            where m.conversa.id = :conversaId
            """)
    Page<MensagemResumo> listarResumosPorConversa(@Param("conversaId") Long conversaId, Pageable pageable);
}
