package tech.jannotti.billing.core.batch.config;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import tech.jannotti.billing.core.batch.jobs.subscription.FinishedSubscriptionsJob;
import tech.jannotti.billing.core.batch.jobs.subscription.SubscriptionInvoicesJob;

@Configuration
public class SubscriptionJobsConfiguration extends AbstractJobsConfiguration {

    @Bean("jobs.subscription.subscriptionInvoices")
    public JobDetailFactoryBean subscriptionInvoicesJob() {
        return createJobDetail(SubscriptionInvoicesJob.class);
    }

    @Bean("jobs.subscription.subscriptionInvoicesTrigger")
    public CronTriggerFactoryBean subscriptionInvoicesTrigger(
        @Qualifier("jobs.subscription.subscriptionInvoices") JobDetail jobDetail,
        @Value("${batch.jobs.subscription.subscriptionInvoices.cron}") String scheduling) {
        return createCronTrigger(jobDetail, scheduling);
    }

    @Bean("jobs.subscription.finishedSubscriptions")
    public JobDetailFactoryBean finishedSubscriptionsJob() {
        return createJobDetail(FinishedSubscriptionsJob.class);
    }

    @Bean("jobs.subscription.finishedSubscriptionsTrigger")
    public CronTriggerFactoryBean finishedSubscriptionsTrigger(
        @Qualifier("jobs.subscription.finishedSubscriptions") JobDetail jobDetail,
        @Value("${batch.jobs.subscription.finishedSubscriptions.cron}") String scheduling) {
        return createCronTrigger(jobDetail, scheduling);
    }

}
