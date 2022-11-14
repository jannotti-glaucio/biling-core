package tech.jannotti.billing.core.batch.jobs.market;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.batch.jobs.AbstractJob;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.services.market.MarketAccountService;

public class MarketBalancesSummaryJob extends AbstractJob {

    private LogManager logManager = LogFactory.getManager(MarketBalancesSummaryJob.class);

    @Autowired
    private MarketAccountService marketService;

    @Override
    public void doExecute(JobExecutionContext executionContext) throws JobExecutionException {

        logManager.logINFO("Executando sumarizacao de saldo de Contas Virtuais");
        marketService.processBalancesSummary();
        logManager.logINFO("Finalizacao sumarizacao de saldo de Contas Virtuais");
    }

}
