package tech.jannotti.billing.core.batch.config.banking;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import tech.jannotti.billing.core.batch.config.AbstractJobsConfiguration;
import tech.jannotti.billing.core.batch.jobs.banking.bb.BankDischargesJob;
import tech.jannotti.billing.core.batch.jobs.banking.bb.BankRemittancesJob;
import tech.jannotti.billing.core.batch.jobs.banking.bb.UnpaidBankBilletsJob;

@Configuration
public class BancoBrasilJobsConfiguration extends AbstractJobsConfiguration {

    @Bean("jobs.banking.bb.bankRemittances")
    public JobDetailFactoryBean bankRemittancesJob() {
        return createJobDetail(BankRemittancesJob.class);
    }

    @Bean("jobs.banking.bb.bankRemittancesTrigger")
    public CronTriggerFactoryBean bankRemittancesTrigger(
        @Qualifier("jobs.banking.bb.bankRemittances") JobDetail jobDetail,
        @Value("${batch.jobs.banking.bb.bankRemittances.cron}") String scheduling) {

        return createCronTrigger(jobDetail, scheduling);
    }

    @Bean("jobs.banking.bb.bankDischarges")
    public JobDetailFactoryBean bankDischargesJob() {
        return createJobDetail(BankDischargesJob.class);
    }

    @Bean("jobs.banking.bb.bankDischargesTrigger")
    public CronTriggerFactoryBean bankDischargesTrigger(
        @Qualifier("jobs.banking.bb.bankDischarges") JobDetail jobDetail,
        @Value("${batch.jobs.banking.bb.bankDischarges.cron}") String scheduling) {

        return createCronTrigger(jobDetail, scheduling);
    }

    @Bean("jobs.banking.bb.unpaidBankBillets")
    public JobDetailFactoryBean unpaidBankBilletsJob() {
        return createJobDetail(UnpaidBankBilletsJob.class);
    }

    @Bean("jobs.banking.bb.unpaidBankBilletsTrigger")
    public CronTriggerFactoryBean unpaidBankBilletsTrigger(
        @Qualifier("jobs.banking.bb.unpaidBankBillets") JobDetail jobDetail,
        @Value("${batch.jobs.banking.bb.unpaidBankBillets.cron}") String scheduling) {

        return createCronTrigger(jobDetail, scheduling);
    }

}
