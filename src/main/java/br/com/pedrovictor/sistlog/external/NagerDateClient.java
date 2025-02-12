package br.com.pedrovictor.sistlog.external;

import br.com.pedrovictor.sistlog.dto.HolidayDTO;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.http.HttpResponse;
import java.util.List;

@FeignClient(name = "nager-date", url = "https://date.nager.at")
public interface NagerDateClient {
    @GetMapping("/api/v3/PublicHolidays/{year}/{countryCode}")
    List<HolidayDTO> getHolidays(@PathVariable int year, @PathVariable String countryCode);

    @GetMapping("/api/v3/isTodayPublicHoliday/{countryCode}")
    Response getIsTodayPublicHoliday(@PathVariable String countryCode);
}
