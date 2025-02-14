package br.com.pedrovictor.sistlog.repository;

import br.com.pedrovictor.sistlog.domain.Pack;
import br.com.pedrovictor.sistlog.domain.PackStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Query("SELECT p.id FROM Pack p WHERE p.status = :status AND p.updatedAt < :dateTime")
    List<Long> findOldCancelledPackageIds(PackStatus status, LocalDateTime dateTime);

    @Modifying
    @Transactional
    @Query("DELETE FROM Pack p WHERE p.id IN :ids")
    void deleteBatchByIds(List<Long> ids);
}
