package tech.jannotti.billing.core.batch.config;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import tech.jannotti.billing.core.batch.jobs.market.MarketBalancesSummaryJob;

@Configuration
public class MarketJobsConfiguration extends AbstractJobsConfiguration {

    @Bean("jobs.market.marketBalancesSummary")
    public JobDetailFactoryBean marketBalancesSummaryJob() {
        return createJobDetail(MarketBalancesSummaryJob.class);
    }

    @Bean("jobs.market.marketBalancesSummaryTrigger")
    public CronTriggerFactoryBean marketBalancesSummaryTrigger(
        @Qualifier("jobs.market.marketBalancesSummary") JobDetail jobDetail,
        @Value("${batch.jobs.market.marketBalancesSummary.cron}") String scheduling) {
        return createCronTrigger(jobDetail, scheduling);
    }

}
