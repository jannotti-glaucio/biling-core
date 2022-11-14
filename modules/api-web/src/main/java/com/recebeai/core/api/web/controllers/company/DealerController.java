package tech.jannotti.billing.core.api.web.controllers.company;

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
import tech.jannotti.billing.core.api.web.controllers.dto.request.dealer.AddDealerRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.request.dealer.UpdateDealerRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.response.dealer.AddDealerRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.dealer.FindDealersRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.dealer.GetDealerRestResponse;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.commons.util.StringHelper;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.rest.validation.ValidateBody;
import tech.jannotti.billing.core.rest.validation.ValidateParameters;
import tech.jannotti.billing.core.services.dealer.DealerMarketService;
import tech.jannotti.billing.core.services.dealer.DealerService;
import tech.jannotti.billing.core.services.dto.request.DealerServiceRequest;
import tech.jannotti.billing.core.services.dto.request.address.AddressServiceRequest;
import tech.jannotti.billing.core.services.dto.request.address.UpdateAddressServiceRequest;
import tech.jannotti.billing.core.services.dto.request.bank.BankAccountServiceRequest;
import tech.jannotti.billing.core.services.dto.request.bank.UpdateBankAccountServiceRequest;
import tech.jannotti.billing.core.validation.extension.annotations.FilterLength;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;
import tech.jannotti.billing.core.validation.extension.annotations.ValidSortDirection;

@RestController("company.dealerController")
@RequestMapping(ApiConstants.V1_API_PATH + "company/dealer")
public class DealerController extends AbstractWebController {

    @Autowired
    private DealerService dealerService;

    @Autowired
    private DealerMarketService dealerMarketService;

    @GetMapping("/")
    @InfoLogging
    @ValidateParameters
    public FindDealersRestResponse find(
        @RequestParam(name = FILTER_PARAMETER, required = false) @FilterLength String filter,
        @RequestParam(name = PAGE_PARAMETER, required = false) @ValidInteger String page,
        @RequestParam(name = SIZE_PARAMETER, required = false) @ValidInteger String size,
        @RequestParam(name = SORT_PARAMETER, required = false) String sort,
        @RequestParam(name = DIRECTION_PARAMETER, required = false) @ValidSortDirection String direction) {
        BaseCompany company = getLoggedCompany();

        if (StringHelper.isBlanks(filter, page, size, sort, direction)) {
            // Consulta sem paginacao
            List<BaseDealer> dealers = dealerService.find(company);
            BaseResultCode resultCode = getQueryResultCode(dealers);
            return new FindDealersRestResponse(resultCode, dtoMapper, dealers);

        } else {
            // Consulta paginada
            PageRequest pageRequest = createPageRequest(page, size, sort, direction);
            Page<BaseDealer> dealers = dealerService.find(company, filter, pageRequest);
            BaseResultCode resultCode = getQueryResultCode(dealers.getContent());
            return new FindDealersRestResponse(resultCode, dtoMapper, dealers);
        }

    }

    @GetMapping("/billingPlan/{token}")
    @InfoLogging
    public FindDealersRestResponse findByBillingPlan(@PathVariable String token,
        @RequestParam(name = PAGE_PARAMETER, required = false) @ValidInteger String page,
        @RequestParam(name = SIZE_PARAMETER, required = false) @ValidInteger String size,
        @RequestParam(name = SORT_PARAMETER, required = false) String sort,
        @RequestParam(name = DIRECTION_PARAMETER, required = false) @ValidSortDirection String direction) {
        BaseCompany company = getLoggedCompany();

        PageRequest pageRequest = createPageRequest(page, size, sort, direction);
        Page<BaseDealer> dealers = dealerService.findByBillingPlan(company, token, pageRequest);

        return new FindDealersRestResponse(getSuccessResultCode(), dtoMapper, dealers);
    }

    @GetMapping("/{token}")
    @InfoLogging
    public GetDealerRestResponse get(@PathVariable String token) {
        BaseCompany company = getLoggedCompany();

        BaseDealer dealer = dealerService.getAndLoad(company, token);
        long currentBalance = dealerMarketService.getCurrentBalance(dealer);

        return new GetDealerRestResponse(getSuccessResultCode(), dtoMapper, currentBalance, dealer);
    }

    @PostMapping
    @InfoLogging
    @ValidateBody
    public AddDealerRestResponse add(@RequestBody AddDealerRestRequest request) {
        BaseCompany company = getLoggedCompany();

        DealerServiceRequest serviceRequest = dtoMapper.map(request, DealerServiceRequest.class);
        List<AddressServiceRequest> serviceAddresses = dtoMapper.mapList(request.getAddresses(), AddressServiceRequest.class);
        List<BankAccountServiceRequest> serviceBankAccounts = dtoMapper.mapList(request.getBankAccounts(),
            BankAccountServiceRequest.class);

        BaseDealer dealer = dealerService.add(company, serviceRequest, serviceAddresses, serviceBankAccounts);
        return new AddDealerRestResponse(getSuccessResultCode(), dealer.getToken());
    }

    @PutMapping("/{token}")
    @InfoLogging
    @ValidateBody
    public RestResponseDTO update(@PathVariable String token, @RequestBody UpdateDealerRestRequest request) {
        BaseCompany company = getLoggedCompany();

        DealerServiceRequest serviceRequest = dtoMapper.map(request, DealerServiceRequest.class);
        List<UpdateAddressServiceRequest> serviceAddresses = dtoMapper.mapList(request.addresses,
            UpdateAddressServiceRequest.class);
        List<UpdateBankAccountServiceRequest> serviceBankAccounts = dtoMapper.mapList(request.bankAccounts,
            UpdateBankAccountServiceRequest.class);

        dealerService.update(company, token, serviceRequest, serviceAddresses, serviceBankAccounts);
        return createSuccessResponse();
    }

    @DeleteMapping("/{token}")
    @InfoLogging
    public RestResponseDTO delete(@PathVariable String token) {
        BaseCompany company = getLoggedCompany();

        dealerService.delete(company, token);
        return createSuccessResponse();
    }

}
