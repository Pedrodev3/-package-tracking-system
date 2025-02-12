package br.com.pedrovictor.sistlog.external.facade;

import br.com.pedrovictor.sistlog.dto.FunFact.FunFactDTO;
import br.com.pedrovictor.sistlog.external.DogApiClient;
import br.com.pedrovictor.sistlog.external.NagerDateClient;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

@Service
@EnableAsync
@AllArgsConstructor
public class ExternalApisFacade {
    private final NagerDateClient nagerClient;
    private final DogApiClient dogApiClient;

    @Async
    public CompletableFuture<Boolean> isHolidayAsync(int year, String countryCode, LocalDate estimatedDeliveryDate ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateInString = estimatedDeliveryDate.format(formatter);

        return CompletableFuture.supplyAsync(() -> nagerClient.getHolidays(year, countryCode).stream().anyMatch(holiday -> holiday.getDate().equals(dateInString)));
    }

    // Preciso refatorar para remover quantidade de DTOs desnecessários
    @Async
    public CompletableFuture<String> getRandomFunFactAsync() {
        return CompletableFuture.supplyAsync(() -> {
            FunFactDTO funFact = dogApiClient.getFunFact();
            return funFact.getData().getFirst().getAttributes().getBody();
        });
    }
}
