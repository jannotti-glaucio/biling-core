package tech.jannotti.billing.core.api.web.controllers.company;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import tech.jannotti.billing.core.api.web.controllers.dto.request.user.AddDealerUserRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.request.user.UpdateUserPasswordRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.request.user.UpdateUserRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.response.user.AddUserRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.user.FindUsersRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.user.GetUserRestResponse;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.persistence.model.base.user.BaseDealerUser;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.rest.validation.ValidateBody;
import tech.jannotti.billing.core.rest.validation.ValidateParameters;
import tech.jannotti.billing.core.services.dealer.DealerService;
import tech.jannotti.billing.core.services.dto.request.user.AddUserServiceRequest;
import tech.jannotti.billing.core.services.dto.request.user.UpdateUserServiceRequest;
import tech.jannotti.billing.core.services.user.DealerUserService;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidDealer;

@RestController("company.dealerUserController")
@RequestMapping(ApiConstants.V1_API_PATH + "company/dealer/user")
public class DealerUserController extends AbstractWebController {

    @Autowired
    private DealerUserService dealerUserService;

    @Autowired
    private DealerService dealerService;

    @GetMapping("/")
    @InfoLogging
    @ValidateParameters
    public FindUsersRestResponse find(
        @RequestParam(name = "dealer", required = false) @ValidDealer String dealer) {
        BaseCompany company = getLoggedCompany();

        BaseDealer dealerEntity = dealerService.get(company, dealer);
        List<BaseDealerUser> users = dealerUserService.find(dealerEntity);

        BaseResultCode resultCode = getQueryResultCode(users);
        return new FindUsersRestResponse(resultCode, dtoMapper, users);
    }

    @GetMapping("/{token}")
    @InfoLogging
    public GetUserRestResponse get(@PathVariable String token) {
        BaseCompany company = getLoggedCompany();

        BaseDealerUser user = dealerUserService.get(company, token);
        return new GetUserRestResponse(getSuccessResultCode(), dtoMapper, user);
    }

    @PostMapping
    @InfoLogging
    @ValidateBody
    public AddUserRestResponse add(@RequestBody AddDealerUserRestRequest request) {
        BaseCompany company = getLoggedCompany();

        BaseDealer dealer = dealerService.get(company, request.getDealer());
        AddUserServiceRequest serviceRequest = dtoMapper.map(request, AddUserServiceRequest.class);

        BaseDealerUser user = dealerUserService.add(dealer, serviceRequest);

        return new AddUserRestResponse(getSuccessResultCode(), user.getToken());
    }

    @PutMapping("/{token}")
    @InfoLogging
    @ValidateBody
    public RestResponseDTO update(@PathVariable String token, @RequestBody UpdateUserRestRequest request) {
        BaseCompany company = getLoggedCompany();

        UpdateUserServiceRequest serviceRequest = dtoMapper.map(request, UpdateUserServiceRequest.class);

        dealerUserService.update(company, token, serviceRequest);
        return createSuccessResponse();
    }

    @PutMapping("/{token}/password")
    @InfoLogging
    @ValidateBody
    public RestResponseDTO updatePassword(@PathVariable String token, @RequestBody UpdateUserPasswordRestRequest request) {
        BaseCompany company = getLoggedCompany();

        dealerUserService.updatePassword(company, token, request.getPassword());
        return createSuccessResponse();
    }

    @PutMapping("/{token}/block")
    @InfoLogging
    @ValidateBody
    public RestResponseDTO block(@PathVariable String token) {
        BaseCompany company = getLoggedCompany();

        dealerUserService.block(company, token);
        return createSuccessResponse();
    }

    @PutMapping("/{token}/unblock")
    @InfoLogging
    @ValidateBody
    public RestResponseDTO unblock(@PathVariable String token) {
        BaseCompany company = getLoggedCompany();

        dealerUserService.unblock(company, token);
        return createSuccessResponse();
    }

    @DeleteMapping("/{token}")
    @InfoLogging
    public RestResponseDTO delete(@PathVariable String token) {
        BaseCompany company = getLoggedCompany();

        dealerUserService.delete(company, token);
        return createSuccessResponse();
    }

}
