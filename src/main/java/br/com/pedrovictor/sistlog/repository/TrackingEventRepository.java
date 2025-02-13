package br.com.pedrovictor.sistlog.repository;

import br.com.pedrovictor.sistlog.domain.Pack;
import br.com.pedrovictor.sistlog.domain.TrackingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackingEventRepository extends JpaRepository<TrackingEvent, Long> {
    List<TrackingEvent> findByPkg(Pack pkg);
}
