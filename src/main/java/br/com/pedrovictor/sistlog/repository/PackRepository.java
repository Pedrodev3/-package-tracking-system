package br.com.pedrovictor.sistlog.repository;

import br.com.pedrovictor.sistlog.domain.Pack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackRepository extends JpaRepository<Pack, Long> {
}
