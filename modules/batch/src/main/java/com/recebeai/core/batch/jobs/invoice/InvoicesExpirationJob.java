package tech.jannotti.billing.core.batch.jobs.invoice;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.batch.jobs.AbstractJob;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.services.batch.InvoiceBatchService;

public class InvoicesExpirationJob extends AbstractJob {

    private LogManager logManager = LogFactory.getManager(InvoicesExpirationJob.class);

    @Autowired
    private InvoiceBatchService invoiceBatchService;

    @Override
    public void doExecute(JobExecutionContext executionContext) throws JobExecutionException {

        logManager.logINFO("Executando expiracao de faturas nao pagas");
        invoiceBatchService.processExpireds();
        logManager.logINFO("Finalizando expiracao de faturas nao pagas");
    }

}
