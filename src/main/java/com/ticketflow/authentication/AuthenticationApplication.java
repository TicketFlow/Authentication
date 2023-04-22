package com.ticketflow.authentication;

import com.ticketflow.authentication.configuration.VaultSecretsConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(VaultSecretsConfiguration.class)
public class AuthenticationApplication implements CommandLineRunner {

    private final VaultSecretsConfiguration vaultSecretsConfiguration;

    public AuthenticationApplication(VaultSecretsConfiguration vaultSecretsConfiguration) {
        this.vaultSecretsConfiguration = vaultSecretsConfiguration;
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("secret 1:" + vaultSecretsConfiguration.getClient_secret());
        log.info("secret 2:" + vaultSecretsConfiguration.getClient_id());
        log.info("secret 3:" + vaultSecretsConfiguration.getTicketFlow());
    }
}
