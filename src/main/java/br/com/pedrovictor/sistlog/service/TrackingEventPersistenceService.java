package br.com.pedrovictor.sistlog.service;

import br.com.pedrovictor.sistlog.domain.TrackingEvent;
import br.com.pedrovictor.sistlog.repository.TrackingEventRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrackingEventPersistenceService {
    private static final Logger logger = LoggerFactory.getLogger(TrackingEventPersistenceService.class);
    private final TrackingEventRepository trackingEventRepository;

    @Transactional
    public void saveTrackingEvent(TrackingEvent trackingEvent) {
        logger.info("Saving tracking event for package ID: {}", trackingEvent.getPkg().getId());
        trackingEventRepository.save(trackingEvent);
    }
}
