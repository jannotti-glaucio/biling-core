package tech.jannotti.billing.core.batch.config.banking;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import tech.jannotti.billing.core.batch.config.AbstractJobsConfiguration;
import tech.jannotti.billing.core.batch.jobs.banking.santander.BankDischargesJob;
import tech.jannotti.billing.core.batch.jobs.banking.santander.BankRemittancesJob;
import tech.jannotti.billing.core.batch.jobs.banking.santander.UnpaidBankBilletsJob;

@Configuration
public class SantanderJobsConfiguration extends AbstractJobsConfiguration {

    @Bean("jobs.banking.santander.bankRemittances")
    public JobDetailFactoryBean bankRemittancesJob() {
        return createJobDetail(BankRemittancesJob.class);
    }

    @Bean("jobs.banking.santander.bankRemittancesTrigger")
    public CronTriggerFactoryBean bankRemittancesTrigger(
        @Qualifier("jobs.banking.santander.bankRemittances") JobDetail jobDetail,
        @Value("${batch.jobs.banking.santander.bankRemittances.cron}") String scheduling) {

        return createCronTrigger(jobDetail, scheduling);
    }

    @Bean("jobs.banking.santander.bankDischarges")
    public JobDetailFactoryBean bankDischargesJob() {
        return createJobDetail(BankDischargesJob.class);
    }

    @Bean("jobs.banking.santander.bankDischargesTrigger")
    public CronTriggerFactoryBean bankDischargesTrigger(
        @Qualifier("jobs.banking.santander.bankDischarges") JobDetail jobDetail,
        @Value("${batch.jobs.banking.santander.bankDischarges.cron}") String scheduling) {

        return createCronTrigger(jobDetail, scheduling);
    }

    @Bean("jobs.banking.santander.unpaidBankBillets")
    public JobDetailFactoryBean unpaidBankBilletsJob() {
        return createJobDetail(UnpaidBankBilletsJob.class);
    }

    @Bean("jobs.banking.santander.unpaidBankBilletsTrigger")
    public CronTriggerFactoryBean unpaidBankBilletsTrigger(
        @Qualifier("jobs.banking.santander.unpaidBankBillets") JobDetail jobDetail,
        @Value("${batch.jobs.banking.santander.unpaidBankBillets.cron}") String scheduling) {

        return createCronTrigger(jobDetail, scheduling);
    }

}
