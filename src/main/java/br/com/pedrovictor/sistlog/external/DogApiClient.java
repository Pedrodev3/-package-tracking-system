package br.com.pedrovictor.sistlog.external;

import br.com.pedrovictor.sistlog.dto.FunFact.FunFactDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name= "dog-api", url = "https://dogapi.dog")
public interface DogApiClient {
    @GetMapping("/api/v2/facts")
    FunFactDTO getFunFact();
}
