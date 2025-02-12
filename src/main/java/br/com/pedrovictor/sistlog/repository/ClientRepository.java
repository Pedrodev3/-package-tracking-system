package br.com.pedrovictor.sistlog.repository;

import br.com.pedrovictor.sistlog.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findClientById(Long id);
    Optional<Client> findByRecipient(String client);
}
