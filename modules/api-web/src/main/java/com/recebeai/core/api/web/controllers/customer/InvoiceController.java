package tech.jannotti.billing.core.api.web.controllers.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.commons.http.HttpConstants;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.controllers.AbstractController;
import tech.jannotti.billing.core.services.InvoiceService;

@RestController("customer.invoiceController")
@RequestMapping(ApiConstants.V1_API_PATH + "customer/invoice")
public class InvoiceController extends AbstractController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/{token}/bankbillet")
    @InfoLogging
    public ResponseEntity<byte[]> printBankBillet(@PathVariable String token) throws Exception {

        // TODO Devolver uma mensagem mais amigavel se o token nao existir, jah que o usuario final vai usar
        byte[] content = invoiceService.printBankBillet(token);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_DISPOSITION, HttpConstants.CONTENT_DISPOSITION_ATTACHMENT_PREFIX +
            ApiConstants.PAYMENT_BANK_BILLET_PDF_FILE_NAME);
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return buildOkResponseEntity(content, responseHeaders);
    }

}
