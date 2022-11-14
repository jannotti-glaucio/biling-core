package tech.jannotti.billing.core.api.web.controllers.dealer;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.api.web.controllers.AbstractWebController;
import tech.jannotti.billing.core.api.web.controllers.dto.request.invoice.AddInvoiceRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.request.invoice.GetInvoiceFeesRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.request.invoice.UpdateInvoiceRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.response.alert.FindInvoiceAlertsRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.invoice.AddInvoiceRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.invoice.FindInvoicesRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.invoice.GetInvoiceFeesRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.invoice.GetInvoiceRestResponse;
import tech.jannotti.billing.core.commons.enums.InvoiceDateToFilterEnum;
import tech.jannotti.billing.core.commons.http.HttpConstants;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.persistence.enums.InvoiceStatusEnum;
import tech.jannotti.billing.core.persistence.enums.MediaTypeEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.alert.BaseInvoiceAlert;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.rest.validation.ValidateBody;
import tech.jannotti.billing.core.rest.validation.ValidateParameters;
import tech.jannotti.billing.core.services.AlertingService;
import tech.jannotti.billing.core.services.InvoiceService;
import tech.jannotti.billing.core.services.dto.request.invoice.AddInvoiceServiceRequest;
import tech.jannotti.billing.core.services.dto.request.invoice.GetInvoiceFeesServiceRequest;
import tech.jannotti.billing.core.services.dto.request.invoice.UpdateInvoiceServiceRequest;
import tech.jannotti.billing.core.services.dto.response.invoice.GetInvoiceFeesServiceResponse;
import tech.jannotti.billing.core.services.dto.response.invoice.SummarizeInvoicesServiceResponse;
import tech.jannotti.billing.core.validation.extension.annotations.FilterLength;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ValidDate;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;
import tech.jannotti.billing.core.validation.extension.annotations.ValidSortDirection;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidInvoiceDateToFilter;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidInvoiceStatusList;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidMediaType;

@RestController("dealer.invoiceController")
@RequestMapping(ApiConstants.V1_API_PATH + "dealer/invoice")
public class InvoiceController extends AbstractWebController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private AlertingService alertingService;

    @GetMapping("/")
    @InfoLogging
    @ValidateParameters
    public FindInvoicesRestResponse find(
        @RequestParam(name = "startDate", required = false) @NotBlankParameter @ValidDate String startDate,
        @RequestParam(name = "endDate", required = false) @NotBlankParameter @ValidDate String endDate,
        @RequestParam(name = "dateToFilter", required = false) @ValidInvoiceDateToFilter String dateToFilter,
        @RequestParam(name = "status", required = false) @ValidInvoiceStatusList String[] status,
        @RequestParam(name = FILTER_PARAMETER, required = false) @FilterLength String filter,
        @RequestParam(name = PAGE_PARAMETER, required = false) @ValidInteger String page,
        @RequestParam(name = SIZE_PARAMETER, required = false) @ValidInteger String size,
        @RequestParam(name = SORT_PARAMETER, required = false) String sort,
        @RequestParam(name = DIRECTION_PARAMETER, required = false) @ValidSortDirection String direction) {
        BaseDealer dealer = getLoggedDealer();

        LocalDate startLocalDate = DateTimeHelper.parseFromIsoDate(startDate);
        LocalDate endLocalDate = DateTimeHelper.parseFromIsoDate(endDate);
        InvoiceDateToFilterEnum dateToFilterEnum = InvoiceDateToFilterEnum.valueOfOrNull(dateToFilter);
        InvoiceStatusEnum[] statusEnum = InvoiceStatusEnum.valueOfArray(status);

        PageRequest pageRequest = createPageRequest(page, size, sort, direction);
        Page<BaseInvoice> invoices = invoiceService.find(dealer, startLocalDate, endLocalDate, dateToFilterEnum, statusEnum,
            filter, pageRequest);

        BaseResultCode resultCode = getQueryResultCode(invoices.getContent());
        return new FindInvoicesRestResponse(resultCode, dtoMapper, invoices);
    }

    @GetMapping("/report/")
    @InfoLogging
    @ValidateParameters
    public FindInvoicesRestResponse find(
        @RequestParam(name = "startDate", required = false) @NotBlankParameter @ValidDate String startDate,
        @RequestParam(name = "endDate", required = false) @NotBlankParameter @ValidDate String endDate,
        @RequestParam(name = "dateToFilter", required = false) @ValidInvoiceDateToFilter String dateToFilter,
        @RequestParam(name = "status", required = false) @ValidInvoiceStatusList String[] status,
        @RequestParam(name = FILTER_PARAMETER, required = false) @FilterLength String filter) {
        BaseDealer dealer = getLoggedDealer();

        LocalDate startLocalDate = DateTimeHelper.parseFromIsoDate(startDate);
        LocalDate endLocalDate = DateTimeHelper.parseFromIsoDate(endDate);
        InvoiceDateToFilterEnum dateToFilterEnum = InvoiceDateToFilterEnum.valueOfOrNull(dateToFilter);
        InvoiceStatusEnum[] statusEnum = InvoiceStatusEnum.valueOfArray(status);

        List<BaseInvoice> invoices = invoiceService.find(dealer, startLocalDate, endLocalDate, dateToFilterEnum, statusEnum,
            filter);

        SummarizeInvoicesServiceResponse summary = invoiceService.summarize(dealer, startLocalDate, endLocalDate,
            dateToFilterEnum, statusEnum, filter);

        BaseResultCode resultCode = getQueryResultCode(invoices);
        return new FindInvoicesRestResponse(resultCode, dtoMapper, invoices, summary.getTotalPaidAmount(), summary.getTotalFees(),
            summary.getTotalNetAmount());
    }

    @GetMapping("/expired/")
    @InfoLogging
    @ValidateParameters
    public FindInvoicesRestResponse findExpired(
        @RequestParam(name = FILTER_PARAMETER, required = false) @FilterLength String filter,
        @RequestParam(name = PAGE_PARAMETER, required = false) @ValidInteger String page,
        @RequestParam(name = SIZE_PARAMETER, required = false) @ValidInteger String size,
        @RequestParam(name = SORT_PARAMETER, required = false) String sort,
        @RequestParam(name = DIRECTION_PARAMETER, required = false) @ValidSortDirection String direction) {
        BaseDealer dealer = getLoggedDealer();

        PageRequest pageRequest = createPageRequest(page, size, sort, direction);
        Page<BaseInvoice> invoices = invoiceService.findExpired(dealer, filter, pageRequest);

        BaseResultCode resultCode = getQueryResultCode(invoices.getContent());
        return new FindInvoicesRestResponse(resultCode, dtoMapper, invoices);
    }

    @GetMapping("/expiring/")
    @InfoLogging
    @ValidateParameters
    public FindInvoicesRestResponse findExpiring(
        @RequestParam(name = "currentDate", required = false) @NotBlankParameter @ValidDate String currentDate,
        @RequestParam(name = FILTER_PARAMETER, required = false) @FilterLength String filter,
        @RequestParam(name = PAGE_PARAMETER, required = false) @ValidInteger String page,
        @RequestParam(name = SIZE_PARAMETER, required = false) @ValidInteger String size,
        @RequestParam(name = SORT_PARAMETER, required = false) String sort,
        @RequestParam(name = DIRECTION_PARAMETER, required = false) @ValidSortDirection String direction) {
        BaseDealer dealer = getLoggedDealer();

        LocalDate currentLocalDate = DateTimeHelper.parseFromIsoDate(currentDate);

        PageRequest pageRequest = createPageRequest(page, size, sort, direction);
        Page<BaseInvoice> invoices = invoiceService.findExpiring(dealer, currentLocalDate, filter, pageRequest);

        BaseResultCode resultCode = getQueryResultCode(invoices.getContent());
        return new FindInvoicesRestResponse(resultCode, dtoMapper, invoices);
    }

    @GetMapping("/{token}")
    @InfoLogging
    public GetInvoiceRestResponse get(@PathVariable String token) {
        BaseDealer dealer = getLoggedDealer();

        BaseInvoice invoice = invoiceService.getAndLoadAndSummarize(dealer, token);
        return new GetInvoiceRestResponse(getSuccessResultCode(), dtoMapper, invoice);
    }

    @GetMapping("/{token}/alerts")
    @InfoLogging
    @ValidateParameters
    public FindInvoiceAlertsRestResponse findAlerts(@PathVariable String token) {
        BaseDealer dealer = getLoggedDealer();

        BaseInvoice invoice = invoiceService.get(dealer, token);
        List<BaseInvoiceAlert> invoiceAlerts = alertingService.findDeliveredInvoiceAlerts(invoice);

        BaseResultCode resultCode = getQueryResultCode(invoiceAlerts);
        return new FindInvoiceAlertsRestResponse(resultCode, dtoMapper, invoiceAlerts);
    }

    @PutMapping("/fees")
    @InfoLogging
    @ValidateBody
    public GetInvoiceFeesRestResponse getFees(@RequestBody GetInvoiceFeesRestRequest request) {
        BaseDealer dealer = getLoggedDealer();

        GetInvoiceFeesServiceRequest serviceRequest = dtoMapper.map(request, GetInvoiceFeesServiceRequest.class);
        GetInvoiceFeesServiceResponse serviceResponse = invoiceService.getFees(dealer, serviceRequest);

        return new GetInvoiceFeesRestResponse(getSuccessResultCode(), serviceResponse.getFees(), serviceResponse.getNetAmount());
    }

    @PostMapping
    @InfoLogging
    @ValidateBody
    public AddInvoiceRestResponse add(@RequestBody AddInvoiceRestRequest request) {

        AddInvoiceServiceRequest serviceRequest = dtoMapper.map(request, AddInvoiceServiceRequest.class);
        BaseInvoice invoice = invoiceService.add(serviceRequest);

        return new AddInvoiceRestResponse(getSuccessResultCode(), invoice.getToken());
    }

    @GetMapping("/{token}/bankbillet")
    @InfoLogging
    public ResponseEntity<byte[]> printBankBillet(@PathVariable String token) throws Exception {
        BaseDealer dealer = getLoggedDealer();

        byte[] content = invoiceService.printBankBillet(dealer, token);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_DISPOSITION, HttpConstants.CONTENT_DISPOSITION_ATTACHMENT_PREFIX +
            ApiConstants.PAYMENT_BANK_BILLET_PDF_FILE_NAME);
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return buildOkResponseEntity(content, responseHeaders);
    }

    @PutMapping("/{token}")
    @InfoLogging
    @ValidateBody
    public RestResponseDTO update(@PathVariable String token, @RequestBody UpdateInvoiceRestRequest request) {
        BaseDealer dealer = getLoggedDealer();

        UpdateInvoiceServiceRequest serviceRequest = dtoMapper.map(request, UpdateInvoiceServiceRequest.class);
        invoiceService.update(dealer, token, serviceRequest);

        return createSuccessResponse();
    }

    @PutMapping("/{token}/send/{media}")
    @InfoLogging
    @ValidateParameters
    public RestResponseDTO send(@PathVariable String token, @PathVariable("media") @ValidMediaType String media) {
        BaseDealer dealer = getLoggedDealer();

        MediaTypeEnum mediaType = MediaTypeEnum.valueOf(media.toUpperCase());

        invoiceService.sendAlert(dealer, token, mediaType);
        return createSuccessResponse();
    }

    @DeleteMapping("/{token}")
    @InfoLogging
    public RestResponseDTO cancel(@PathVariable String token) {
        BaseDealer dealer = getLoggedDealer();

        invoiceService.cancel(dealer, token);
        return createSuccessResponse();
    }

}
