package br.com.pedrovictor.sistlog.controller;

import br.com.pedrovictor.sistlog.dto.PackDTOs.*;
import br.com.pedrovictor.sistlog.dto.PackDTOs.PackDetails.PackDetailsResponseDTO;
import br.com.pedrovictor.sistlog.service.PackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/packages")
@RequiredArgsConstructor
public class PackController implements SwaggerPackController {

    private static final Logger logger = LoggerFactory.getLogger(PackController.class);

    private final PackService packService;

    @Override
    public ResponseEntity<PackCreateResponseDTO> createPackage(@Valid @RequestBody PackCreateRequestDTO body) {
        logger.info("Receiving request to create a package: {}", body);
        PackCreateResponseDTO createdPackage = packService.createPackage(body);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPackage);
    }

    @Override
    public ResponseEntity<PackUpdateStatusResponseDTO> updatePackageStatus(
            @PathVariable Long id,
            @Valid @RequestBody PackUpdateStatusRequestDTO body) {
        logger.info("Receiving request to update the status of the package with ID: {}", id);
        PackUpdateStatusResponseDTO updatedPackage = packService.updatePackageStatusById(id, body);

        return ResponseEntity.status(HttpStatus.OK).body(updatedPackage);
    }

    @Override
    public ResponseEntity<PackDetailsResponseDTO> getPackageDetails(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean includeEvents) {
        logger.info("Receiving request to get the details of the package with ID: {}", id);
        PackDetailsResponseDTO packageDetails = packService.getPackageDetailsById(id, includeEvents);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Cache-Control", "public, max-age=600")
                .body(packageDetails);
    }

    @Override
    public ResponseEntity<List<PackDetailsResponseDTO>> getPackages(
            @RequestParam(required = false) List<String> recipients,
            @RequestParam(required = false) List<String> senders) {
        logger.info("Receiving request to get the packages of the recipient: {} and sender: {}", recipients, senders);
        List<PackDetailsResponseDTO> packages = packService.getPackagesByRecipientsAndOrSenders(recipients, senders);

        if (packages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header("Cache-Control", "public, max-age=300")
                .body(packages);
    }

    @Override
    public  ResponseEntity<PackCancelledResponseDTO> cancelPackage(@PathVariable Long id) {
        logger.info("Receiving request to cancel the package with ID: {}", id);
        PackCancelledResponseDTO cancelledPackage = packService.cancelPackageById(id);

        return ResponseEntity.status(HttpStatus.OK).body(cancelledPackage);
    }
}
