package br.com.pedrovictor.sistlog.repository;

import br.com.pedrovictor.sistlog.domain.Sender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SenderRepository extends JpaRepository<Sender, Long> {
    Optional<Sender> findSenderById(Long id);
    Optional<Sender> findBySender(String sender);
}
