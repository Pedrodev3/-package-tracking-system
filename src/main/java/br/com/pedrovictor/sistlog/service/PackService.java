package br.com.pedrovictor.sistlog.service;

import br.com.pedrovictor.sistlog.domain.*;
import br.com.pedrovictor.sistlog.dto.PackDTOs.*;
import br.com.pedrovictor.sistlog.dto.PackDTOs.PackDetails.EventDTO;
import br.com.pedrovictor.sistlog.dto.PackDTOs.PackDetails.PackDetailsResponseDTO;
import br.com.pedrovictor.sistlog.exception.BadRequestException;
import br.com.pedrovictor.sistlog.exception.NotFoundException;
import br.com.pedrovictor.sistlog.external.facade.ExternalApisFacade;
import br.com.pedrovictor.sistlog.repository.ClientRepository;
import br.com.pedrovictor.sistlog.repository.PackRepository;
import br.com.pedrovictor.sistlog.repository.SenderRepository;
import br.com.pedrovictor.sistlog.repository.TrackingEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PackService {

    private final ExternalApisFacade externalApisFacade;
    private final PackRepository packRepository;
    private final ClientRepository clientRepository;
    private final SenderRepository senderRepository;
    private final TrackingEventRepository trackingEventRepository;
    private final TrackingEventService trackingEventService;

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

    public PackUpdateStatusResponseDTO updatePackageStatusById(Long id, PackUpdateStatusRequestDTO packUpdateStatus) {
        try {
            Pack pack = packRepository.findByIdWithDetails(id)
                    .orElseThrow(() -> new NotFoundException("Pacote não foi encontrado"));

            PackStatus newStatus = PackStatus.fromLabel(packUpdateStatus.getStatus());
            validateStatusUpdate(pack, newStatus);

            if(!pack.getStatus().equals(newStatus)) {
                pack.setStatus(newStatus);

                if(newStatus == PackStatus.DELIVERED) {
                    pack.setDeliveredAt(LocalDateTime.now());
                }

                packRepository.save(pack);
            }

            /* Send tracking event */
            this.trackingEventService.sendTrackingEvent(pack, newStatus);

            return new PackUpdateStatusResponseDTO(
                    pack.getId(),
                    pack.getDescription(),
                    pack.getSender().getSender(),
                    pack.getRecipient().getRecipient(),
                    pack.getStatus().name(),
                    pack.getCreatedAt(),
                    pack.getUpdatedAt(),
                    pack.getDeliveredAt()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error updating package status", e);
        }
    }

    public PackDetailsResponseDTO getPackageDetailsById(Long id, Boolean includeEvents) {
        try {
            Pack pack = includeEvents
                    ? packRepository.findByIdWithEvents(id)
                    .orElseThrow(() -> new NotFoundException("Pacote não foi encontrado"))
                    : packRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Pacote não foi encontrado"));

            return new PackDetailsResponseDTO(
                    pack.getId(),
                    pack.getDescription(),
                    pack.getSender().getSender(),
                    pack.getRecipient().getRecipient(),
                    pack.getStatus().name(),
                    pack.getCreatedAt(),
                    pack.getUpdatedAt(),
                    includeEvents ? getPackEvents(pack) : null
            );
        } catch (Exception e) {
            throw new RuntimeException("Error getting package details", e);
        }
    }

    public List<PackDetailsResponseDTO> getPackagesByRecipientsAndOrSenders(List<String> recipients, List<String> senders) {
        try {
            List<Pack> packs = packRepository.findByRecipientsAndOrSenders(recipients, senders);
            return packs.stream()
                    .map(pack -> new PackDetailsResponseDTO(
                            pack.getId(),
                            pack.getDescription(),
                            pack.getSender().getSender(),
                            pack.getRecipient().getRecipient(),
                            pack.getStatus().name(),
                            pack.getCreatedAt(),
                            pack.getUpdatedAt(),
                            null
                    ))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error getting packages", e);
        }
    }

    public PackCancelledResponseDTO cancelPackageById(Long id) {
        try {
            Pack pack = packRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Pacote não foi encontrado"));

            PackStatus newStatus = PackStatus.CANCELLED;
            validateStatusUpdate(pack, newStatus);

            pack.setStatus(newStatus);
            packRepository.save(pack);

            return new PackCancelledResponseDTO(
                    pack.getId(),
                    pack.getStatus().name(),
                    pack.getUpdatedAt()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error deleting package", e);
        }
    }

    private CompletableFuture<Boolean> getHolidayFuture(LocalDate estimatedDeliveryDate) {
        return externalApisFacade.isHolidayAsync(LocalDate.now().getYear(), "BR", estimatedDeliveryDate);
    }
    private CompletableFuture<String> getFunFactFuture() {
        return externalApisFacade.getRandomFunFactAsync();
    }

    private void validateStatusUpdate(Pack pack, PackStatus newStatus) {
        PackStatus oldStatus = pack.getStatus();
        if (oldStatus == PackStatus.DELIVERED) {
            throw new BadRequestException(
                    "O pacote já foi entregue e não pode ser cancelado."
            );
        }
        if(oldStatus == PackStatus.CANCELLED) {
            throw new BadRequestException(
                    "O pacote já foi cancelado."
            );
        }
        if (oldStatus == newStatus) {
            throw new BadRequestException(
                    String.format("O status já é %s".formatted(newStatus.getLabelPtBr()))
            );
        }
        if(!oldStatus.canUpdateTO(newStatus)) {
            throw new BadRequestException(
                    String.format("Não é permitido alteração de status de %s para %s".formatted(oldStatus.getLabelPtBr(), newStatus.getLabelPtBr()))
            );
        }
    }

    private List<EventDTO> getPackEvents(Pack pack) {
        List<TrackingEvent> trackingEvents = trackingEventRepository.findByPkg(pack);
        return trackingEvents.stream()
                .map(trackingEvent -> new EventDTO(
                        trackingEvent.getPkg().getId(),
                        trackingEvent.getEventLocation(),
                        trackingEvent.getEventDescription(),
                        trackingEvent.getEventTimestamp()
                ))
                .toList();
    }
}
