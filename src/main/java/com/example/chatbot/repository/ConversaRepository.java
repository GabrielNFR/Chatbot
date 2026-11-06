package com.example.chatbot.repository;

import com.example.chatbot.dto.ConversaResumo;
import com.example.chatbot.model.Conversa;
import com.example.chatbot.model.enums.StatusConversa;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConversaRepository extends JpaRepository<Conversa, Long> {
    List<Conversa> findByStatusOrderByIniciadaEmAscIdAsc(StatusConversa status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Conversa c where c.id = :id")
    Optional<Conversa> buscarParaAtualizar(@Param("id") Long id);

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
