package br.com.pedrovictor.sistlog.scheduler;

import br.com.pedrovictor.sistlog.service.DataPurgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Year;

@Component
@RequiredArgsConstructor
public class DataPurgeScheduler {

    private final DataPurgeService dataPurgeService;

    @Scheduled(cron = "0 0 0 1 1 *")
    public void purgeOldData() {
        dataPurgeService.purgeOldCancelledPackages();
    }
}
