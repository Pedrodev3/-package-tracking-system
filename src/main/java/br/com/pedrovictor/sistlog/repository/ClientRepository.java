package br.com.pedrovictor.sistlog.repository;

import br.com.pedrovictor.sistlog.domain.Client;
import br.com.pedrovictor.sistlog.domain.Sender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByRecipient(String client);
    Optional<Client> findFirstByRecipient(String client);
}
