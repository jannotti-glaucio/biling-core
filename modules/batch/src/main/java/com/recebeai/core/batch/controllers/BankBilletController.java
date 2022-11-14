package tech.jannotti.billing.core.batch.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.services.payment.BankBilletService;

@RestController
@RequestMapping("bankBillet")
public class BankBilletController extends AbstractBatchController {

    @Autowired
    private BankBilletService bankBilletService;

    @DeleteMapping("/{token}")
    @InfoLogging
    public RestResponseDTO cancel(@PathVariable String token) {

        bankBilletService.forceCancelation(token);
        return createSuccessResponse();
    }

}
