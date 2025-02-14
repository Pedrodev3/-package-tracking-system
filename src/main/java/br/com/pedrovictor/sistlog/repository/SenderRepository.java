package br.com.pedrovictor.sistlog.repository;

import br.com.pedrovictor.sistlog.domain.Sender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SenderRepository extends JpaRepository<Sender, Long> {
    Optional<Sender> findSenderById(Long id);
    Optional<Sender> findBySender(String sender);
    Optional<Sender> findFirstBySender(String sender);
}
