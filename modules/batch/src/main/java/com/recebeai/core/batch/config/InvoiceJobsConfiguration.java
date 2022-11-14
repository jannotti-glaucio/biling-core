package tech.jannotti.billing.core.batch.config;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import tech.jannotti.billing.core.batch.jobs.invoice.InvoicesAlertingJob;
import tech.jannotti.billing.core.batch.jobs.invoice.InvoicesExpirationJob;

@Configuration
public class InvoiceJobsConfiguration extends AbstractJobsConfiguration {

    @Bean("jobs.invoice.invoicesExpiration")
    public JobDetailFactoryBean invoicesExpirationJob() {
        return createJobDetail(InvoicesExpirationJob.class);
    }

    @Bean("jobs.invoice.invoicesExpirationTrigger")
    public CronTriggerFactoryBean invoicesExpirationTrigger(
        @Qualifier("jobs.invoice.invoicesExpiration") JobDetail jobDetail,
        @Value("${batch.jobs.invoice.invoicesExpiration.cron}") String scheduling) {
        return createCronTrigger(jobDetail, scheduling);
    }

    @Bean("jobs.invoice.invoicesAlerting")
    public JobDetailFactoryBean invoicesAlertingJob() {
        return createJobDetail(InvoicesAlertingJob.class);
    }

    @Bean("jobs.invoice.invoicesAlertingTrigger")
    public CronTriggerFactoryBean invoicesAlertingTrigger(
        @Qualifier("jobs.invoice.invoicesAlerting") JobDetail jobDetail,
        @Value("${batch.jobs.invoice.invoicesAlerting.cron}") String scheduling) {
        return createCronTrigger(jobDetail, scheduling);
    }

}
