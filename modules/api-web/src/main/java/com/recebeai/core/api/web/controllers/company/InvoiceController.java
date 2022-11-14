package tech.jannotti.billing.core.api.web.controllers.company;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.api.web.controllers.AbstractWebController;
import tech.jannotti.billing.core.api.web.controllers.dto.response.invoice.FindInvoicesRestResponse;
import tech.jannotti.billing.core.commons.enums.InvoiceDateToFilterEnum;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.persistence.enums.InvoiceStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.validation.ValidateParameters;
import tech.jannotti.billing.core.services.InvoiceService;
import tech.jannotti.billing.core.services.dealer.DealerService;
import tech.jannotti.billing.core.services.dto.response.invoice.SummarizeInvoicesServiceResponse;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ValidDate;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;
import tech.jannotti.billing.core.validation.extension.annotations.ValidSortDirection;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidInvoiceDateToFilter;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidInvoiceStatusList;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidDealer;

@RestController("company.invoiceController")
@RequestMapping(ApiConstants.V1_API_PATH + "company/invoice")
public class InvoiceController extends AbstractWebController {

    @Autowired
    private DealerService dealerService;

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/")
    @InfoLogging
    @ValidateParameters
    public FindInvoicesRestResponse find(
        @RequestParam(name = "dealer", required = false) @NotBlankParameter @ValidDealer String dealer,
        @RequestParam(name = "startDate", required = false) @NotBlankParameter @ValidDate String startDate,
        @RequestParam(name = "endDate", required = false) @NotBlankParameter @ValidDate String endDate,
        @RequestParam(name = "dateToFilter", required = false) @ValidInvoiceDateToFilter String dateToFilter,
        @RequestParam(name = "status", required = false) @ValidInvoiceStatusList String[] status,
        @RequestParam(name = PAGE_PARAMETER, required = false) @ValidInteger String page,
        @RequestParam(name = SIZE_PARAMETER, required = false) @ValidInteger String size,
        @RequestParam(name = SORT_PARAMETER, required = false) String sort,
        @RequestParam(name = DIRECTION_PARAMETER, required = false) @ValidSortDirection String direction) {
        BaseCompany company = getLoggedCompany();

        BaseDealer dealerEntity = dealerService.get(company, dealer);
        LocalDate startLocalDate = DateTimeHelper.parseFromIsoDate(startDate);
        LocalDate endLocalDate = DateTimeHelper.parseFromIsoDate(endDate);
        InvoiceDateToFilterEnum dateToFilterEnum = InvoiceDateToFilterEnum.valueOfOrNull(dateToFilter);
        InvoiceStatusEnum[] statusEnum = InvoiceStatusEnum.valueOfArray(status);

        PageRequest pageRequest = createPageRequest(page, size, sort, direction);
        Page<BaseInvoice> invoices = invoiceService.find(dealerEntity, startLocalDate, endLocalDate, dateToFilterEnum, statusEnum,
            pageRequest);

        BaseResultCode resultCode = getQueryResultCode(invoices.getContent());
        return new FindInvoicesRestResponse(resultCode, dtoMapper, invoices);
    }

    @GetMapping("/report/")
    @InfoLogging
    @ValidateParameters
    public FindInvoicesRestResponse find(
        @RequestParam(name = "dealer", required = false) @NotBlankParameter @ValidDealer String dealer,
        @RequestParam(name = "startDate", required = false) @NotBlankParameter @ValidDate String startDate,
        @RequestParam(name = "endDate", required = false) @NotBlankParameter @ValidDate String endDate,
        @RequestParam(name = "dateToFilter", required = false) @ValidInvoiceDateToFilter String dateToFilter,
        @RequestParam(name = "status", required = false) @ValidInvoiceStatusList String[] status) {
        BaseCompany company = getLoggedCompany();

        BaseDealer dealerEntity = dealerService.get(company, dealer);
        LocalDate startLocalDate = DateTimeHelper.parseFromIsoDate(startDate);
        LocalDate endLocalDate = DateTimeHelper.parseFromIsoDate(endDate);
        InvoiceDateToFilterEnum dateToFilterEnum = InvoiceDateToFilterEnum.valueOfOrNull(dateToFilter);
        InvoiceStatusEnum[] statusEnum = InvoiceStatusEnum.valueOfArray(status);

        List<BaseInvoice> invoices = invoiceService.find(dealerEntity, startLocalDate, endLocalDate, dateToFilterEnum,
            statusEnum);

        SummarizeInvoicesServiceResponse summary = invoiceService.summarize(dealerEntity, startLocalDate, endLocalDate,
            dateToFilterEnum, statusEnum);

        BaseResultCode resultCode = getQueryResultCode(invoices);
        return new FindInvoicesRestResponse(resultCode, dtoMapper, invoices, summary.getTotalPaidAmount(), summary.getTotalFees(),
            summary.getTotalNetAmount());
    }

}
