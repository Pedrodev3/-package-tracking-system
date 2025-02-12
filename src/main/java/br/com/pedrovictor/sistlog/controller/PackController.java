package br.com.pedrovictor.sistlog.controller;

import br.com.pedrovictor.sistlog.dto.PackDTOs.PackCreateRequestDTO;
import br.com.pedrovictor.sistlog.dto.PackDTOs.PackCreateResponseDTO;
import br.com.pedrovictor.sistlog.dto.PackDTOs.PackUpdateStatusRequestDTO;
import br.com.pedrovictor.sistlog.dto.PackDTOs.PackUpdateStatusResponseDTO;
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

    private final PackService packService;

    @PostMapping(value = "/create-package", consumes = "application/json", produces = "application/json")
    public ResponseEntity<PackCreateResponseDTO> createPackage(@Valid @RequestBody PackCreateRequestDTO body) {
        logger.info("Receiving request to create a package: {}", body);
        PackCreateResponseDTO createdPackage = packService.createPackage(body);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPackage);
    }

    @PatchMapping(value = "/{id}/update-status", consumes = "application/json", produces = "application/json")
    public ResponseEntity<PackUpdateStatusResponseDTO> updatePackageStatus(@PathVariable Long id, @Valid @RequestBody PackUpdateStatusRequestDTO body) {
        logger.info("Receiving request to update the status of the package with ID: {}", id);
        PackUpdateStatusResponseDTO updatedPackage = packService.updatePackageStatusById(id, body);

        return ResponseEntity.status(HttpStatus.CREATED).body(updatedPackage);
    }

}
