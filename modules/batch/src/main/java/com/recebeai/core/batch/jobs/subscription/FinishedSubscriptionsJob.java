package tech.jannotti.billing.core.batch.jobs.subscription;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.batch.jobs.AbstractJob;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.services.batch.SubscriptionBatchService;

public class FinishedSubscriptionsJob extends AbstractJob {

    private LogManager logManager = LogFactory.getManager(FinishedSubscriptionsJob.class);

    @Autowired
    private SubscriptionBatchService subscriptionBatchService;

    @Override
    public void doExecute(JobExecutionContext executionContext) throws JobExecutionException {

        logManager.logINFO("Executando processamento de Assinaturas finalizadas");
        subscriptionBatchService.processFinisheds();
        logManager.logINFO("Finalizando processamento de Assinaturas finalizadas");
    }

}
