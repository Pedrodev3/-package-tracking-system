//package br.com.pedrovictor.sistlog;
//
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
//import org.springframework.context.annotation.Bean;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.utility.DockerImageName;
//
//@TestConfiguration(proxyBeanMethods = false)
//@Testcontainers
//class TestcontainersConfiguration {
//
//    @Bean
//    @ServiceConnection
//    MySQLContainer<?> mysqlContainer() {
//        return new MySQLContainer<>(DockerImageName.parse("mysql:8.0.36"))
//                .withDatabaseName("sist_log_test")
//                .withUsername("testuser")
//                .withPassword("testpass");
//    }
//
//}
