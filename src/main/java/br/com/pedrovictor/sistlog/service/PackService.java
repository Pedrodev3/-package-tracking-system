package br.com.pedrovictor.sistlog.service;

import br.com.pedrovictor.sistlog.domain.Client;
import br.com.pedrovictor.sistlog.domain.Pack;
import br.com.pedrovictor.sistlog.domain.PackStatus;
import br.com.pedrovictor.sistlog.domain.Sender;
import br.com.pedrovictor.sistlog.dto.PackDTOs.PackCreateRequestDTO;
import br.com.pedrovictor.sistlog.dto.PackDTOs.PackCreateResponseDTO;
import br.com.pedrovictor.sistlog.external.facade.ExternalApisFacade;
import br.com.pedrovictor.sistlog.repository.ClientRepository;
import br.com.pedrovictor.sistlog.repository.PackRepository;
import br.com.pedrovictor.sistlog.repository.SenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PackService {

    private final ExternalApisFacade externalApisFacade;
    private final PackRepository packRepository;
    private final ClientRepository clientRepository;
    private final SenderRepository senderRepository;

    public PackCreateResponseDTO createPackage(PackCreateRequestDTO body) {
        try {
            Sender sender = senderRepository.findBySender(body.getSender())
                    .orElseGet(() -> {
                        Sender newSender = new Sender();
                        newSender.setSender(body.getSender());
                        return senderRepository.save(newSender);
                    });
            Client recipient = clientRepository.findByRecipient(body.getRecipient())
                    .orElseGet(() -> {
                        Client newClient = new Client();
                        newClient.setRecipient(body.getRecipient());
                        return clientRepository.save(newClient);
                    });

            CompletableFuture<Boolean> holidayFuture = getHolidayFuture(body.getEstimatedDeliveryDate());
            CompletableFuture<String> funFactFuture = getFunFactFuture();
            CompletableFuture.allOf(holidayFuture, funFactFuture).join();

            boolean isHoliday = holidayFuture.join();
            String funFact = funFactFuture.join();

            Pack pack = new Pack();
            pack.setDescription(body.getDescription());
            pack.setFunFact(funFact);
            pack.setSender(sender);
            pack.setRecipient(recipient);
            pack.setStatus(PackStatus.CREATED);
            pack.setIsHoliday(isHoliday);
            pack.setEstimatedDeliveryDate(body.getEstimatedDeliveryDate());
            packRepository.save(pack);

            return new PackCreateResponseDTO(
                    pack.getId(),
                    pack.getDescription(),
                    pack.getSender().getSender(),
                    pack.getRecipient().getRecipient(),
                    pack.getStatus().name(),
                    pack.getCreatedAt(),
                    pack.getUpdatedAt()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error creating package", e);
        }
    }

    private CompletableFuture<Boolean> getHolidayFuture(LocalDate estimatedDeliveryDate) {
        return externalApisFacade.isHolidayAsync(LocalDate.now().getYear(), "BR", estimatedDeliveryDate);
    }
    private CompletableFuture<String> getFunFactFuture() {
        return externalApisFacade.getRandomFunFactAsync();
    }
}
