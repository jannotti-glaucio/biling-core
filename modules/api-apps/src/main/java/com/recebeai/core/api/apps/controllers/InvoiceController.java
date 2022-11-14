package tech.jannotti.billing.core.api.apps.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.api.apps.controllers.dto.request.invoice.AddInvoiceRestRequest;
import tech.jannotti.billing.core.api.apps.controllers.dto.response.invoice.AddBankBilletInvoiceRestResponse;
import tech.jannotti.billing.core.api.apps.controllers.dto.response.invoice.FindPaidInvoicesRestResponse;
import tech.jannotti.billing.core.api.apps.controllers.dto.response.invoice.GetInvoiceRestResponse;
import tech.jannotti.billing.core.commons.enums.InvoiceDateToFilterEnum;
import tech.jannotti.billing.core.commons.http.HttpConstants;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.persistence.enums.PaymentMethodEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseApplication;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.rest.validation.ValidateBody;
import tech.jannotti.billing.core.rest.validation.ValidateParameters;
import tech.jannotti.billing.core.services.InvoiceService;
import tech.jannotti.billing.core.services.dto.request.invoice.AddInvoiceServiceRequest;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ValidDate;

@RestController
@RequestMapping(ApiConstants.V1_API_PATH + "invoice")
public class InvoiceController extends AbstractAppsController {

    @Value("${api.invoice.bankbilletUrl}")
    private String bankBilletUrl;

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/bankbillet/")
    @InfoLogging
    @ValidateBody
    public AddBankBilletInvoiceRestResponse addBankBillet(@RequestBody AddInvoiceRestRequest request) {

        AddInvoiceServiceRequest serviceRequest = dtoMapper.map(request, AddInvoiceServiceRequest.class);
        serviceRequest.setPaymentMethod(PaymentMethodEnum.BANK_BILLET);
        BaseInvoice invoice = invoiceService.add(serviceRequest);

        String url = buildBankBilletUrl(invoice);
        return new AddBankBilletInvoiceRestResponse(getSuccessResultCode(), invoice.getToken(),
            invoice.getBankBillet().getLineCode(), url);
    }

    @GetMapping("/bankbillet/{token}")
    @InfoLogging
    public ResponseEntity<byte[]> printBankBillet(@PathVariable String token) throws Exception {

        byte[] content = invoiceService.printBankBillet(token);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_DISPOSITION, HttpConstants.CONTENT_DISPOSITION_ATTACHMENT_PREFIX +
            ApiConstants.PAYMENT_BANK_BILLET_PDF_FILE_NAME);
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return buildOkResponseEntity(content, responseHeaders);
    }

    @GetMapping("/{token}")
    @InfoLogging
    public GetInvoiceRestResponse get(@PathVariable String token) {
        BaseApplication application = getLoggedApplication();

        BaseInvoice invoice = invoiceService.getAndLoadAndSummarize(application.getDealer(), token);
        if (invoice.getBankBillet() != null) {
            String url = buildBankBilletUrl(invoice);
            invoice.getBankBillet().setUrl(url);
        }

        return new GetInvoiceRestResponse(getSuccessResultCode(), dtoMapper, invoice);
    }

    @DeleteMapping("/{token}")
    @InfoLogging
    public RestResponseDTO cancel(@PathVariable String token) {
        BaseApplication application = getLoggedApplication();

        invoiceService.cancel(application.getDealer(), token);
        return createSuccessResponse();
    }

    @GetMapping("/paid/")
    @InfoLogging
    @ValidateParameters
    public FindPaidInvoicesRestResponse findPaid(
        @RequestParam(name = "paymentDate", required = false) @NotBlankParameter @ValidDate String paymentDate) {
        BaseApplication application = getLoggedApplication();

        LocalDate paymentLocalDate = DateTimeHelper.parseFromIsoDate(paymentDate);

        List<BaseInvoice> invoices = invoiceService.find(application.getDealer(), paymentLocalDate, paymentLocalDate,
            InvoiceDateToFilterEnum.PAYMENT, null);

        BaseResultCode resultCode = getQueryResultCode(invoices);
        return new FindPaidInvoicesRestResponse(resultCode, dtoMapper, invoices);
    }

    private String buildBankBilletUrl(BaseInvoice invoice) {
        return bankBilletUrl + "/" + invoice.getToken();
    }

}
