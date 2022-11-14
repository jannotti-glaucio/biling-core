package tech.jannotti.billing.core.batch.jobs.banking.bb;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.banking.bb.exchange.cnab400.CNAB400DischargeLoader;
import tech.jannotti.billing.core.banking.bb.exchange.cnab400.CNAB400DischargeProcessor;
import tech.jannotti.billing.core.batch.jobs.AbstractJob;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;

public class BankDischargesJob extends AbstractJob {

    private LogManager logManager = LogFactory.getManager(BankDischargesJob.class);

    @Autowired
    private CNAB400DischargeLoader cnab400DischargeLoader;

    @Autowired
    private CNAB400DischargeProcessor cnab400DischargeProcessor;

    @Override
    public void doExecute(JobExecutionContext executionContext) throws JobExecutionException {

        logManager.logINFO("Executando carregamento de arquivos CNAB400 de retorno do Banco do Brasil");
        cnab400DischargeLoader.execute();
        logManager.logINFO("Finalizando carregamento de arquivos CNAB400 de retorno do Banco do Brasil");

        logManager.logINFO("Executando processamento de arquivos CNAB400 de retorno do Banco do Brasil");
        cnab400DischargeProcessor.execute();
        logManager.logINFO("Finalizando processamento de arquivos CNAB400 de retorno do Banco do Brasil");
    }

}
