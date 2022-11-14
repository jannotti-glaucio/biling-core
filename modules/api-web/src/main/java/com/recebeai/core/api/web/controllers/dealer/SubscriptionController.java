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
import tech.jannotti.billing.core.api.web.controllers.dto.request.order.subscription.AddSubscriptionRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.request.order.subscription.UpdateSubscriptionRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.response.order.subscription.AddSubscriptionRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.order.subscription.FindSubscriptionsRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.order.subscription.GetSubscriptionRestResponse;
import tech.jannotti.billing.core.commons.enums.InvoiceDateToFilterEnum;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.persistence.enums.SubscriptionStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.order.BaseOrderSubscription;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.rest.validation.ValidateBody;
import tech.jannotti.billing.core.rest.validation.ValidateParameters;
import tech.jannotti.billing.core.services.dto.request.order.subscription.AddSubscriptionServiceRequest;
import tech.jannotti.billing.core.services.dto.request.order.subscription.UpdateSubscriptionServiceRequest;
import tech.jannotti.billing.core.services.order.SubscriptionService;
import tech.jannotti.billing.core.validation.extension.annotations.FilterLength;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ValidDate;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidInvoiceDateToFilter;
import tech.jannotti.billing.core.validation.extension.annotations.enums.order.ValidSubscriptionStatusList;

@RestController("dealer.subscriptionController")
@RequestMapping(ApiConstants.V1_API_PATH + "dealer/subscription")
public class SubscriptionController extends AbstractWebController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/")
    @InfoLogging
    @ValidateParameters
    public FindSubscriptionsRestResponse find(
        @RequestParam(name = "startDate", required = false) @NotBlankParameter @ValidDate String startDate,
        @RequestParam(name = "endDate", required = false) @NotBlankParameter @ValidDate String endDate,
        @RequestParam(name = "dateToFilter", required = false) @ValidInvoiceDateToFilter String dateToFilter,
        @RequestParam(name = "status", required = false) @ValidSubscriptionStatusList String[] status,
        @RequestParam(name = FILTER_PARAMETER, required = false) @FilterLength String filter,
        @RequestParam(name = PAGE_PARAMETER, required = false) @ValidInteger String page,
        @RequestParam(name = SIZE_PARAMETER, required = false) @ValidInteger String size) {

        BaseDealer dealer = getLoggedDealer();

        InvoiceDateToFilterEnum dateToFilterEnum = InvoiceDateToFilterEnum.valueOfOrNull(dateToFilter);
        LocalDate startLocalDate = DateTimeHelper.parseFromIsoDate(startDate);
        LocalDate endLocalDate = DateTimeHelper.parseFromIsoDate(endDate);
        SubscriptionStatusEnum[] statusEnum = SubscriptionStatusEnum.valueOfArray(status);

        PageRequest pageRequest = createPageRequest(page, size);
        Page<BaseOrderSubscription> subscriptions = subscriptionService.find(dealer, startLocalDate, endLocalDate,
            dateToFilterEnum, statusEnum, filter, pageRequest);

        BaseResultCode resultCode = getQueryResultCode(subscriptions.getContent());
        return new FindSubscriptionsRestResponse(resultCode, dtoMapper, subscriptions);
    }

    @GetMapping("/report")
    @InfoLogging
    @ValidateParameters
    public FindSubscriptionsRestResponse find(
        @RequestParam(name = "startDate", required = false) @NotBlankParameter @ValidDate String startDate,
        @RequestParam(name = "endDate", required = false) @NotBlankParameter @ValidDate String endDate,
        @RequestParam(name = "dateToFilter", required = false) @ValidInvoiceDateToFilter String dateToFilter,
        @RequestParam(name = "status", required = false) @ValidSubscriptionStatusList String[] status,
        @RequestParam(name = FILTER_PARAMETER, required = false) @FilterLength String filter) {

        BaseDealer dealer = getLoggedDealer();

        InvoiceDateToFilterEnum dateToFilterEnum = InvoiceDateToFilterEnum.valueOfOrNull(dateToFilter);
        LocalDate startLocalDate = DateTimeHelper.parseFromIsoDate(startDate);
        LocalDate endLocalDate = DateTimeHelper.parseFromIsoDate(endDate);
        SubscriptionStatusEnum[] statusEnum = SubscriptionStatusEnum.valueOfArray(status);

        List<BaseOrderSubscription> subscriptions = subscriptionService.find(dealer, startLocalDate, endLocalDate,
            dateToFilterEnum, statusEnum, filter);

        BaseResultCode resultCode = getQueryResultCode(subscriptions);
        return new FindSubscriptionsRestResponse(resultCode, dtoMapper, subscriptions);
    }

    @GetMapping("/{token}")
    @InfoLogging
    public GetSubscriptionRestResponse get(@PathVariable String token) {
        BaseDealer dealer = getLoggedDealer();

        BaseOrderSubscription subscription = subscriptionService.getAndLoad(dealer, token);
        return new GetSubscriptionRestResponse(getSuccessResultCode(), dtoMapper, subscription);
    }

    @PostMapping
    @InfoLogging
    @ValidateBody
    public AddSubscriptionRestResponse add(@RequestBody AddSubscriptionRestRequest request) {

        AddSubscriptionServiceRequest serviceRequest = dtoMapper.map(request, AddSubscriptionServiceRequest.class);
        BaseOrderSubscription subscription = subscriptionService.add(serviceRequest);

        return new AddSubscriptionRestResponse(getSuccessResultCode(), subscription.getToken());
    }

    @PutMapping("/{token}")
    @InfoLogging
    @ValidateBody
    public RestResponseDTO update(@PathVariable String token, @RequestBody UpdateSubscriptionRestRequest request) {
        BaseDealer dealer = getLoggedDealer();

        UpdateSubscriptionServiceRequest serviceRequest = dtoMapper.map(request, UpdateSubscriptionServiceRequest.class);
        subscriptionService.update(dealer, token, serviceRequest);

        return createSuccessResponse();
    }

    @PutMapping("/{token}/suspend")
    @InfoLogging
    public RestResponseDTO suspend(@PathVariable String token) {
        BaseDealer dealer = getLoggedDealer();

        subscriptionService.suspend(dealer, token);
        return createSuccessResponse();
    }

    @PutMapping("/{token}/reopen")
    @InfoLogging
    public RestResponseDTO reopen(@PathVariable String token) {
        BaseDealer dealer = getLoggedDealer();

        subscriptionService.reopen(dealer, token);
        return createSuccessResponse();
    }

    @DeleteMapping("/{token}")
    @InfoLogging
    public RestResponseDTO cancel(@PathVariable String token) {
        BaseDealer dealer = getLoggedDealer();

        subscriptionService.cancel(dealer, token);
        return createSuccessResponse();
    }

}
