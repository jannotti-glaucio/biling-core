package tech.jannotti.billing.core.batch.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;

@DisallowConcurrentExecution
public abstract class AbstractJob implements Job {

    private static final LogManager logManager = LogFactory.getManager(AbstractJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            doExecute(context);
        } catch (Exception e) {
            String jobName = context.getJobDetail().getKey().getName();
            logManager.logERROR("Erro executando Job [%s]", e, jobName);
            throw new JobExecutionException(e);
        }
    }

    public abstract void doExecute(JobExecutionContext context) throws Exception;
}
