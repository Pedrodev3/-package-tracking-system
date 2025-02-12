package br.com.pedrovictor.sistlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "br.com.pedrovictor.sistlog.external")
public class SistLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistLogApplication.class, args);
    }

}
