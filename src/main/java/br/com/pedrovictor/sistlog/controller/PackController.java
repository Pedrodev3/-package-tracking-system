package br.com.pedrovictor.sistlog.controller;

import br.com.pedrovictor.sistlog.dto.PackCreateDTO;
import br.com.pedrovictor.sistlog.service.PackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/packages")
@RequiredArgsConstructor
public class PackController {

    private static final Logger logger = LoggerFactory.getLogger(PackController.class);

    private final PackService packageService;

    @PostMapping(value = "/create-package", consumes = "application/json", produces = "application/json")
    public ResponseEntity<PackCreateDTO> createPackage(@Valid @RequestBody PackCreateDTO body) {
        PackCreateDTO createdPackage = packageService.createPackage(body);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPackage);
    }
}
