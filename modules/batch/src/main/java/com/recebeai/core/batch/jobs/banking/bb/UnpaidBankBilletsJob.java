package tech.jannotti.billing.core.batch.jobs.banking.bb;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.banking.bb.exchange.UnpaidBankBilletsProcessor;
import tech.jannotti.billing.core.batch.jobs.AbstractJob;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;

public class UnpaidBankBilletsJob extends AbstractJob {

    private LogManager logManager = LogFactory.getManager(UnpaidBankBilletsJob.class);

    @Autowired
    private UnpaidBankBilletsProcessor unpaidBankBilletsProcessor;

    @Override
    public void doExecute(JobExecutionContext executionContext) throws JobExecutionException {

        logManager.logINFO("Executando processamento de boletos nao pagos do Banco do Brasil");
        unpaidBankBilletsProcessor.execute();
        logManager.logINFO("Finalizando processamento de boletos nao pagos do Banco do Brasil");
    }

}
