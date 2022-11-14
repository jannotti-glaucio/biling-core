package tech.jannotti.billing.core.api.apps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "tech.jannotti.billing" })
public class AppsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppsApiApplication.class, args);
    }
}