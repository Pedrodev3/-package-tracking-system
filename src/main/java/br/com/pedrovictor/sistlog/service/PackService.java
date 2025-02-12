package br.com.pedrovictor.sistlog.service;

import br.com.pedrovictor.sistlog.domain.Client;
import br.com.pedrovictor.sistlog.domain.Pack;
import br.com.pedrovictor.sistlog.domain.Sender;
import br.com.pedrovictor.sistlog.dto.PackCreateDTO;
import br.com.pedrovictor.sistlog.repository.ClientRepository;
import br.com.pedrovictor.sistlog.repository.PackRepository;
import br.com.pedrovictor.sistlog.repository.SenderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PackService {

    private final PackRepository packRepository;
    private final ClientRepository clientRepository;
    private final SenderRepository senderRepository;

    public PackCreateDTO createPackage(PackCreateDTO body) {
        try {
            Sender sender = senderRepository.findBySender(body.getSender())
                    .orElseThrow(() -> new RuntimeException("Sender not found"));

            Client recipient = clientRepository.findByRecipient(body.getRecipient())
                    .orElseThrow(() -> new RuntimeException("Recipient not found"));

            Pack pack = new Pack();
            pack.setDescription(body.getDescription());
            pack.setFunFact(body.getFunFact());
            pack.setSender(sender);
            pack.setRecipient(recipient);
            pack.setIsHoliday(body.getIsHoliday());
            pack.setEstimatedDeliveryDate(body.getEstimatedDeliveryDate());
            packRepository.save(pack);

            return body;
        } catch (Exception e) {
            throw new RuntimeException("Error creating package");
        }
    }
}
