package br.com.pedrovictor.sistlog.service;

import br.com.pedrovictor.sistlog.controller.PackController;
import br.com.pedrovictor.sistlog.domain.PackStatus;
import br.com.pedrovictor.sistlog.repository.PackRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataPurgeService {
    private static final Logger logger = LoggerFactory.getLogger(DataPurgeService.class);
    private final PackRepository packRepository;
    private static final int BATCH_SIZE = 5000;

    @Async
    @Transactional
    public void purgeOldCancelledPackages() {
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        List<Long> packageIds;
        logger.info("Purging packages with status CANCELLED and updated before {}", oneYearAgo);

        do {
            packageIds = packRepository.findOldCancelledPackageIds(PackStatus.CANCELLED, oneYearAgo, BATCH_SIZE);
            if (!packageIds.isEmpty()) {
                packRepository.deleteBatchByIds(packageIds);
                logger.info("Deleted {} old cancelled packages", packageIds.size());

                if (packageIds.size() < BATCH_SIZE) {
                    logger.info("Reducing batch size due to low data volume.");
                }
            }
        } while (!packageIds.isEmpty());

        logger.info("Finished purging old CANCELLED packages.");
    }
}
