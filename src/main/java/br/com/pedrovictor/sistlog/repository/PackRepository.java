package br.com.pedrovictor.sistlog.repository;

import br.com.pedrovictor.sistlog.domain.Pack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackRepository extends JpaRepository<Pack, Long> {
    @Query("SELECT p FROM Pack p " +
            "LEFT JOIN FETCH p.sender " +
            "LEFT JOIN FETCH p.recipient " +
            "WHERE p.id = :id")
    Optional<Pack> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT p FROM Pack p " +
            "LEFT JOIN FETCH p.sender " +
            "LEFT JOIN FETCH p.recipient " +
            "LEFT JOIN FETCH p.events " +
            "WHERE p.id = :id")
    Optional<Pack> findByIdWithEvents(@Param("id") Long id);

    @Query("SELECT p FROM Pack p " +
            "LEFT JOIN FETCH p.sender " +
            "LEFT JOIN FETCH p.recipient " +
            "WHERE (:recipients IS NULL OR p.recipient.recipient IN :recipients) " +
            "AND  (:senders IS NULL OR p.sender.sender IN :senders)")
    List<Pack> findByRecipientsAndOrSenders(List<String> recipients, List<String> senders);
}
