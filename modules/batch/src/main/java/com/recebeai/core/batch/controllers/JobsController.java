package tech.jannotti.billing.core.batch.controllers;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.rest.exception.ControllerException;

@RestController
@RequestMapping("jobs")
public class JobsController extends AbstractBatchController {

    private static final LogManager logManager = LogFactory.getManager(JobsController.class);

    @Autowired
    private Scheduler scheduler;

    @RequestMapping(value = "/{jobName}/execute", method = RequestMethod.POST)
    @InfoLogging
    public RestResponseDTO execute(@PathVariable("jobName") String jobName) {

        logManager.logDEBUG("Executando manualmente o Job [%s]", jobName);

        JobKey jobKey = new JobKey("jobs." + jobName);
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            throw new ControllerException("Erro executando manualmente Job [%s]", e, jobName);
        }

        logManager.logDEBUG("Finalizando execucao manual do Job [%s]", jobName);
        return createSuccessResponse();
    }

}
