package tech.jannotti.billing.core.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import tech.jannotti.billing.core.batch.config.QuartzConfiguration;
import tech.jannotti.billing.core.batch.config.banking.BancoBrasilJobsConfiguration;

@Import({ QuartzConfiguration.class, BancoBrasilJobsConfiguration.class })
@SpringBootApplication(scanBasePackages = { "tech.jannotti.billing" })
public class BatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }

}