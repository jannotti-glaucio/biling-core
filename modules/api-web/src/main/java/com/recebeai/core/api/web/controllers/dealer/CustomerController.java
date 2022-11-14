package tech.jannotti.billing.core.api.web.controllers.dealer;

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
import tech.jannotti.billing.core.api.web.controllers.dto.request.customer.AddCustomerRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.request.customer.UpdateCustomerRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.response.customer.AddCustomerRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.customer.FindCustomersRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.customer.GetCustomerRestResponse;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomer;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.rest.validation.ValidateBody;
import tech.jannotti.billing.core.rest.validation.ValidateParameters;
import tech.jannotti.billing.core.services.customer.CustomerService;
import tech.jannotti.billing.core.services.dto.request.CustomerServiceRequest;
import tech.jannotti.billing.core.services.dto.request.address.AddressServiceRequest;
import tech.jannotti.billing.core.services.dto.request.address.UpdateAddressServiceRequest;
import tech.jannotti.billing.core.validation.extension.annotations.FilterLength;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;
import tech.jannotti.billing.core.validation.extension.annotations.ValidSortDirection;

@RestController("dealer.customerController")
@RequestMapping(ApiConstants.V1_API_PATH + "dealer/customer")
public class CustomerController extends AbstractWebController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/")
    @InfoLogging
    @ValidateParameters
    public FindCustomersRestResponse find(
        @RequestParam(name = FILTER_PARAMETER, required = false) @FilterLength String filter,
        @RequestParam(name = PAGE_PARAMETER, required = false) @ValidInteger String page,
        @RequestParam(name = SIZE_PARAMETER, required = false) @ValidInteger String size,
        @RequestParam(name = SORT_PARAMETER, required = false) String sort,
        @RequestParam(name = DIRECTION_PARAMETER, required = false) @ValidSortDirection String direction) {
        BaseDealer dealer = getLoggedDealer();

        PageRequest pageRequest = createPageRequest(page, size, sort, direction);
        Page<BaseCustomer> customers = customerService.find(dealer, filter, pageRequest);

        BaseResultCode resultCode = getQueryResultCode(customers.getContent());
        return new FindCustomersRestResponse(resultCode, dtoMapper, customers);
    }

    @GetMapping("/{token}")
    @InfoLogging
    public GetCustomerRestResponse get(@PathVariable String token) {
        BaseDealer dealer = getLoggedDealer();

        BaseCustomer customer = customerService.getAndLoadAddresses(dealer, token);
        return new GetCustomerRestResponse(getSuccessResultCode(), dtoMapper, customer);
    }

    @PostMapping
    @InfoLogging
    @ValidateBody
    public AddCustomerRestResponse add(@RequestBody AddCustomerRestRequest request) {
        BaseDealer dealer = getLoggedDealer();

        CustomerServiceRequest serviceRequest = dtoMapper.map(request, CustomerServiceRequest.class);
        List<AddressServiceRequest> serviceAddresses = dtoMapper.mapList(request.getAddresses(), AddressServiceRequest.class);

        BaseCustomer customer = customerService.add(dealer, serviceRequest, serviceAddresses);
        return new AddCustomerRestResponse(getSuccessResultCode(), customer.getToken());
    }

    @PutMapping("/{token}")
    @InfoLogging
    @ValidateBody
    public RestResponseDTO update(@PathVariable String token, @RequestBody UpdateCustomerRestRequest request) {
        BaseDealer dealer = getLoggedDealer();

        CustomerServiceRequest serviceRequest = dtoMapper.map(request, CustomerServiceRequest.class);
        List<UpdateAddressServiceRequest> serviceAddresses = dtoMapper.mapList(request.getAddresses(),
            UpdateAddressServiceRequest.class);

        customerService.update(dealer, token, serviceRequest, serviceAddresses);
        return createSuccessResponse();
    }

    @DeleteMapping("/{token}")
    @InfoLogging
    public RestResponseDTO delete(@PathVariable String token) {
        BaseDealer dealer = getLoggedDealer();

        customerService.delete(dealer, token);
        return createSuccessResponse();
    }

}
