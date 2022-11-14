package tech.jannotti.billing.core.batch.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.services.NotificationService;

@RestController
@RequestMapping("notification")
public class NotificationController extends AbstractBatchController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/resend/undelivered")
    @InfoLogging
    public RestResponseDTO resendUndelivered() {

        notificationService.resendUndelivered();
        return createSuccessResponse();
    }

}
