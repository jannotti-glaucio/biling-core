package tech.jannotti.billing.core.api.web.controllers.company;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.api.web.controllers.AbstractWebController;
import tech.jannotti.billing.core.api.web.controllers.dto.request.market.DenyMarketWithdrawRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.response.market.FindDealerMarketWithdrawsRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.market.FindMarketStatementsRestResponse;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.persistence.enums.MarketWithdrawStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketStatement;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketWithdraw;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.persistence.model.base.user.BaseUser;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.rest.validation.ValidateBody;
import tech.jannotti.billing.core.rest.validation.ValidateParameters;
import tech.jannotti.billing.core.services.dealer.DealerMarketService;
import tech.jannotti.billing.core.services.dealer.DealerService;
import tech.jannotti.billing.core.services.dto.response.market.SummarizeMarketWithdrawsServiceResponse;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ValidDate;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;
import tech.jannotti.billing.core.validation.extension.annotations.ValidSortDirection;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidMarketWithdrawStatusList;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidDealer;

@RestController("company.dealerMarketController")
@RequestMapping(ApiConstants.V1_API_PATH + "company/dealer/market")
public class DealerMarketController extends AbstractWebController {

    @Autowired
    private DealerService dealerService;

    @Autowired
    private DealerMarketService dealerMarketService;

    @GetMapping("/statement/{dealerToken}/")
    @InfoLogging
    @ValidateParameters
    public FindMarketStatementsRestResponse findStatementsByDealer(
        @PathVariable String dealerToken,
        @RequestParam(name = "startDate", required = false) @NotBlankParameter @ValidDate String startDate,
        @RequestParam(name = "endDate", required = false) @NotBlankParameter @ValidDate String endDate) {
        BaseCompany company = getLoggedCompany();

        BaseDealer dealer = dealerService.get(company, dealerToken);

        LocalDate startLocalDate = DateTimeHelper.parseFromIsoDate(startDate);
        LocalDate endLocalDate = DateTimeHelper.parseFromIsoDate(endDate);

        List<BaseMarketStatement> statements = dealerMarketService.findStatements(dealer, startLocalDate, endLocalDate);
        long currentBalance = dealerMarketService.getCurrentBalance(dealer);

        BaseResultCode resultCode = getQueryResultCode(statements);
        return new FindMarketStatementsRestResponse(resultCode, dtoMapper, currentBalance, statements);
    }

    @GetMapping("/withdraw/")
    @InfoLogging
    @ValidateParameters
    public FindDealerMarketWithdrawsRestResponse findWithdraws(
        @RequestParam(name = "startDate", required = false) @NotBlankParameter @ValidDate String startDate,
        @RequestParam(name = "endDate", required = false) @NotBlankParameter @ValidDate String endDate,
        @RequestParam(name = "dealer", required = false) @ValidDealer String dealer,
        @RequestParam(name = "status", required = false) @ValidMarketWithdrawStatusList String[] status,
        @RequestParam(name = PAGE_PARAMETER, required = false) @ValidInteger String page,
        @RequestParam(name = SIZE_PARAMETER, required = false) @ValidInteger String size,
        @RequestParam(name = SORT_PARAMETER, required = false) String sort,
        @RequestParam(name = DIRECTION_PARAMETER, required = false) @ValidSortDirection String direction) {
        BaseCompany company = getLoggedCompany();

        LocalDate startLocalDate = DateTimeHelper.parseFromIsoDate(startDate);
        LocalDate endLocalDate = DateTimeHelper.parseFromIsoDate(endDate);
        BaseDealer dealerEntity = dealerService.getOrNull(company, dealer);
        MarketWithdrawStatusEnum[] statusEnum = MarketWithdrawStatusEnum.valueOfArray(status);

        PageRequest pageRequest = createPageRequest(page, size, sort, direction);
        Page<BaseMarketWithdraw> withdraws = dealerMarketService.findWithdraws(company, dealerEntity, startLocalDate,
            endLocalDate, statusEnum, pageRequest);

        BaseResultCode resultCode = getQueryResultCode(withdraws.getContent());
        return new FindDealerMarketWithdrawsRestResponse(resultCode, dtoMapper, withdraws);
    }

    @GetMapping("/withdraw/report/")
    @InfoLogging
    @ValidateParameters
    public FindDealerMarketWithdrawsRestResponse findWithdraws(
        @RequestParam(name = "startDate", required = false) @NotBlankParameter @ValidDate String startDate,
        @RequestParam(name = "endDate", required = false) @NotBlankParameter @ValidDate String endDate,
        @RequestParam(name = "dealer", required = false) @ValidDealer String dealer,
        @RequestParam(name = "status", required = false) @ValidMarketWithdrawStatusList String[] status) {
        BaseCompany company = getLoggedCompany();

        LocalDate startLocalDate = DateTimeHelper.parseFromIsoDate(startDate);
        LocalDate endLocalDate = DateTimeHelper.parseFromIsoDate(endDate);
        BaseDealer dealerEntity = dealerService.getOrNull(company, dealer);
        MarketWithdrawStatusEnum[] statusEnum = MarketWithdrawStatusEnum.valueOfArray(status);

        List<BaseMarketWithdraw> withdraws = dealerMarketService.findWithdraws(company, dealerEntity, startLocalDate,
            endLocalDate, statusEnum);

        SummarizeMarketWithdrawsServiceResponse summary = dealerMarketService.summarizeWithdraws(company, dealerEntity,
            startLocalDate, endLocalDate, statusEnum);

        BaseResultCode resultCode = getQueryResultCode(withdraws);
        return new FindDealerMarketWithdrawsRestResponse(resultCode, dtoMapper, withdraws, summary.getTotalAmount(),
            summary.getTotalFees(), summary.getTotalNetAmount());
    }

    @GetMapping("/withdraw/pending/")
    @InfoLogging
    @ValidateParameters
    public FindDealerMarketWithdrawsRestResponse findRequestedWithdraws(
        @RequestParam(name = "dealer", required = false) @ValidDealer String dealer,
        @RequestParam(name = PAGE_PARAMETER, required = false) @ValidInteger String page,
        @RequestParam(name = SIZE_PARAMETER, required = false) @ValidInteger String size,
        @RequestParam(name = SORT_PARAMETER, required = false) String sort,
        @RequestParam(name = DIRECTION_PARAMETER, required = false) @ValidSortDirection String direction) {
        BaseCompany company = getLoggedCompany();

        BaseDealer dealerEntity = dealerService.getOrNull(company, dealer);

        PageRequest pageRequest = createPageRequest(page, size, sort, direction);
        Page<BaseMarketWithdraw> withdraws = dealerMarketService.findPendingWithdraws(company, dealerEntity, pageRequest);

        BaseResultCode resultCode = getQueryResultCode(withdraws.getContent());
        return new FindDealerMarketWithdrawsRestResponse(resultCode, dtoMapper, withdraws);
    }

    @PutMapping("/withdraw/{token}/approve")
    @InfoLogging
    @ValidateBody
    public RestResponseDTO approveWithdraw(@PathVariable String token) {
        BaseCompany company = getLoggedCompany();
        BaseUser user = getLoggedUser();

        dealerMarketService.approveWithdraw(company, token, user);
        return createSuccessResponse();
    }

    @PutMapping("/withdraw/{token}/deny")
    @InfoLogging
    @ValidateBody
    public RestResponseDTO denyWithdraw(@PathVariable String token, @RequestBody DenyMarketWithdrawRestRequest request) {
        BaseCompany company = getLoggedCompany();
        BaseUser user = getLoggedUser();

        dealerMarketService.denyWithdraw(company, token, user, request.getDenyReason());
        return createSuccessResponse();
    }

    @PutMapping("/withdraw/{token}/release")
    @InfoLogging
    @ValidateBody
    public RestResponseDTO provideWithdraw(@PathVariable String token) {
        BaseCompany company = getLoggedCompany();

        dealerMarketService.releaseWithdraw(company, token);
        return createSuccessResponse();
    }

}
