package tech.jannotti.billing.core.api.web.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.api.web.controllers.dto.request.invoice.PayInvoiceRestRequest;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.commons.spring.SpringConstants;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePayment;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.services.InvoiceService;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;
import tech.jannotti.billing.core.services.payment.PaymentService;

@RestController
@RequestMapping(ApiConstants.V1_API_PATH + "tests")
@Profile(SpringConstants.TESTS_PROFILE)
public class TestsController extends AbstractWebController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private PaymentService paymentService;

    @PutMapping("/dealer/invoice/{token}/pay")
    @InfoLogging
    public RestResponseDTO payInvoice(@PathVariable String token, @RequestBody PayInvoiceRestRequest request) {
        BaseDealer dealer = getLoggedDealer();

        BaseInvoice invoice = invoiceService.get(dealer, token);
        if (invoice == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_TOKEN_NOT_FOUND, token);

        BasePayment payment = paymentService.getAuthorized(invoice);
        if (payment == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_TOKEN_NOT_FOUND, token);

        LocalDate paymentDate = DateTimeHelper.parseFromIsoDate(request.getPaymentDate());
        int paidAmount = payment.getAmount();
        int fee = Integer.valueOf(request.getFee());
        LocalDate releaseDate = paymentDate.plusDays(1);
        int releasedAmount = paidAmount - fee;

        paymentService.capture(payment, paymentDate, paidAmount, fee, releaseDate, releasedAmount);
        return createSuccessResponse();
    }

}
