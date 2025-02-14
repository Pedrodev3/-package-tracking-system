package br.com.pedrovictor.sistlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients(basePackages = "br.com.pedrovictor.sistlog.external")
@EnableAsync
@EnableRetry
@EnableScheduling
public class SistLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistLogApplication.class, args);
    }

}
