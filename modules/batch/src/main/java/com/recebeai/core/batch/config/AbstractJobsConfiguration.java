package tech.jannotti.billing.core.batch.config;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

public class AbstractJobsConfiguration {

    protected JobDetailFactoryBean createJobDetail(Class<? extends Job> jobClazz) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClazz);
        factoryBean.setDurability(true);
        return factoryBean;
    }

    protected CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String cronExpression) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(0L);
        factoryBean.setCronExpression(cronExpression);
        return factoryBean;
    }

}
