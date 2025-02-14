package br.com.pedrovictor.sistlog.controller;

import br.com.pedrovictor.sistlog.service.DataPurgeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/data-purge-test")
@AllArgsConstructor
public class DataPurgeTestController {
    private final DataPurgeService dataPurgeService;

    @GetMapping("/purge-old-data")
    public ResponseEntity<String> purgeOldData() {
        dataPurgeService.purgeOldCancelledPackages();
        return ResponseEntity.accepted().body("Purge process started! Check logs for progress.");
    }
}
