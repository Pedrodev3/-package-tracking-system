package br.com.pedrovictor.sistlog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class AsyncConfig {
    @Bean(name = "trackingEventExecutor")
    public Executor trackingEventExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Número mínimo de threads disponíveis
        executor.setMaxPoolSize(10); // Número máximo de threads simultâneas
        executor.setQueueCapacity(20); // Número máximo de tarefas aguardando execução
        executor.setThreadNamePrefix("TrackingEventThread-");
        executor.initialize();

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // Evita descarte de tarefas
        return executor;
    }
}
