package tech.jannotti.billing.core.api.web.controllers.dealer;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import tech.jannotti.billing.core.api.web.controllers.dto.request.market.AddMarketWithdrawRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.request.market.GetMarketWithdrawFeesRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.response.market.AddMarketWithdrawRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.market.FindMarketStatementsRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.market.FindMarketWithdrawsRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.market.GetMarketWithdrawFeesRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.market.GetMarketWithdrawRestResponse;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
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
import tech.jannotti.billing.core.services.dto.request.MarketWithdrawServiceRequest;
import tech.jannotti.billing.core.services.dto.response.market.GetMarketWithdrawFeesServiceResponse;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ValidDate;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;
import tech.jannotti.billing.core.validation.extension.annotations.ValidSortDirection;

@RestController("dealer.dealerMarketController")
@RequestMapping(ApiConstants.V1_API_PATH + "dealer/market")
public class DealerMarketController extends AbstractWebController {

    @Autowired
    private DealerMarketService dealerMarketService;

    @GetMapping("/statement/")
    @InfoLogging
    @ValidateParameters
    public FindMarketStatementsRestResponse findStatementsByDealer(
        @RequestParam(name = "startDate", required = false) @NotBlankParameter @ValidDate String startDate,
        @RequestParam(name = "endDate", required = false) @NotBlankParameter @ValidDate String endDate) {
        BaseDealer dealer = getLoggedDealer();

        // TODO Criar um limite para o periodo da pesquisa (90 dias) e quantidade de registros
        // TODO Validar se a data incial eh menor que a final

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
    public FindMarketWithdrawsRestResponse findWithdraws(
        @RequestParam(name = "startDate", required = false) @NotBlankParameter @ValidDate String startDate,
        @RequestParam(name = "endDate", required = false) @NotBlankParameter @ValidDate String endDate,
        @RequestParam(name = PAGE_PARAMETER, required = false) @ValidInteger String page,
        @RequestParam(name = SIZE_PARAMETER, required = false) @ValidInteger String size,
        @RequestParam(name = SORT_PARAMETER, required = false) String sort,
        @RequestParam(name = DIRECTION_PARAMETER, required = false) @ValidSortDirection String direction) {
        BaseDealer dealer = getLoggedDealer();

        LocalDate startLocalDate = DateTimeHelper.parseFromIsoDate(startDate);
        LocalDate endLocalDate = DateTimeHelper.parseFromIsoDate(endDate);

        PageRequest pageRequest = createPageRequest(page, size, sort, direction);
        Page<BaseMarketWithdraw> withdraws = dealerMarketService.findWithdraws(dealer, startLocalDate, endLocalDate, pageRequest);
        long currentBalance = dealerMarketService.getCurrentBalance(dealer);

        BaseResultCode resultCode = getQueryResultCode(withdraws.getContent());
        return new FindMarketWithdrawsRestResponse(resultCode, dtoMapper, currentBalance, withdraws);
    }

    @GetMapping("/withdraw/{token}")
    @InfoLogging
    @ValidateParameters
    public GetMarketWithdrawRestResponse getWithdraw(@PathVariable String token) {

        BaseMarketWithdraw withdraw = dealerMarketService.getWithdraw(token);

        BaseResultCode resultCode = getQueryResultCode(withdraw);
        return new GetMarketWithdrawRestResponse(resultCode, dtoMapper, withdraw);
    }

    @PutMapping("/withdraw/fees")
    @InfoLogging
    @ValidateBody
    public GetMarketWithdrawFeesRestResponse getWithdrawFees(@RequestBody GetMarketWithdrawFeesRestRequest request) {
        BaseDealer dealer = getLoggedDealer();

        MarketWithdrawServiceRequest serviceRequest = dtoMapper.map(request, MarketWithdrawServiceRequest.class);
        GetMarketWithdrawFeesServiceResponse serviceResponse = dealerMarketService.getWithdrawFees(dealer, serviceRequest);

        return new GetMarketWithdrawFeesRestResponse(getSuccessResultCode(), serviceResponse.getFees(),
            serviceResponse.getNetAmount());
    }

    @PostMapping("/withdraw")
    @InfoLogging
    @ValidateBody
    public AddMarketWithdrawRestResponse addWithdraw(@RequestBody AddMarketWithdrawRestRequest request) {
        BaseDealer dealer = getLoggedDealer();

        BaseUser user = getLoggedUser();
        MarketWithdrawServiceRequest serviceRequest = dtoMapper.map(request, MarketWithdrawServiceRequest.class);

        BaseMarketWithdraw withdraw = dealerMarketService.addWithdraw(dealer, user, serviceRequest);
        return new AddMarketWithdrawRestResponse(getSuccessResultCode(), withdraw.getToken());
    }

    @DeleteMapping("/withdraw/{token}")
    @InfoLogging
    public RestResponseDTO cancelWithdraw(@PathVariable String token) {
        BaseDealer dealer = getLoggedDealer();

        dealerMarketService.cancelWithdraw(dealer, token);
        return createSuccessResponse();
    }

}
