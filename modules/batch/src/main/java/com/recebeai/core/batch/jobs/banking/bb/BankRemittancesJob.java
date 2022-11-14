package tech.jannotti.billing.core.batch.jobs.banking.bb;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.banking.bb.exchange.cnab400.CNAB400RemittanceProcessor;
import tech.jannotti.billing.core.batch.jobs.AbstractJob;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;

public class BankRemittancesJob extends AbstractJob {

    private LogManager logManager = LogFactory.getManager(BankRemittancesJob.class);

    @Autowired
    private CNAB400RemittanceProcessor bankRemittanceProcessor;

    @Override
    public void doExecute(JobExecutionContext executionContext) throws JobExecutionException {

        logManager.logINFO("Executando geracao de arquivos CNAB400 de remessa do Banco do Brasil");
        bankRemittanceProcessor.execute();
        logManager.logINFO("Finalizando geracao de arquivos CNAB400 de remessa do Banco do Brasil");
    }

}
