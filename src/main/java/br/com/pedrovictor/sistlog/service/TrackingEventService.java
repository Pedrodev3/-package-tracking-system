package br.com.pedrovictor.sistlog.service;

import br.com.pedrovictor.sistlog.domain.Pack;
import br.com.pedrovictor.sistlog.domain.PackStatus;
import br.com.pedrovictor.sistlog.domain.TrackingEvent;
import br.com.pedrovictor.sistlog.domain.TrackingEventLocationType;
import br.com.pedrovictor.sistlog.repository.TrackingEventRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TrackingEventService {
    private static final Logger logger = LoggerFactory.getLogger(TrackingEventService.class);
    private final TrackingEventRepository trackingEventRepository;
    @Lazy
    private final ApplicationContext applicationContext;

    @Transactional
    public void saveTrackingEvent(TrackingEvent trackingEvent) {
        trackingEventRepository.save(trackingEvent);
    }

    @Async("trackingEventExecutor")
    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void sendTrackingEvent(Pack pack, PackStatus newStatus) {
        try {
            logger.info("Processing tracking event asynchronously for package ID: {}", pack.getId());

            TrackingEvent trackingEvent = new TrackingEvent();
            trackingEvent.setPkg(pack);

            TrackingEventLocationType locationType = TrackingEventLocationType.fromStatus(newStatus);
            trackingEvent.setEventLocation(locationType.getLocation());
            trackingEvent.setEventDescription(locationType.getDescription());
            trackingEvent.setEventTimestamp(LocalDateTime.now());

            TrackingEventService trackingEventServiceProxy = applicationContext.getBean(TrackingEventService.class);
            trackingEventServiceProxy.saveTrackingEvent(trackingEvent);

            logger.info("Tracking event saved for package ID: {}", pack.getId());
        } catch (Exception e) {
            throw new RuntimeException("Error sending tracking event for package " + pack, e);
        }
//        CompletableFuture.completedFuture(null);
    }

    @Recover
    public CompletableFuture<Void> recoverTrackingEvent(Exception e, Long packageId, String status, String location, String description) {
        logger.error("Failed to save tracking event for package {} after retries: {}", packageId, e.getMessage());
        return CompletableFuture.completedFuture(null);
    }
}
