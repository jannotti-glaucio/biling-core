package tech.jannotti.billing.core.api.apps.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.api.apps.controllers.dto.request.CustomerRestRequest;
import tech.jannotti.billing.core.api.apps.controllers.dto.response.customer.AddCustomerRestResponse;
import tech.jannotti.billing.core.api.apps.controllers.dto.response.customer.GetCustomerRestResponse;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.persistence.model.base.BaseApplication;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomer;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.rest.validation.ValidateBody;
import tech.jannotti.billing.core.services.customer.CustomerService;
import tech.jannotti.billing.core.services.dto.request.CustomerServiceRequest;
import tech.jannotti.billing.core.services.dto.request.address.AddressServiceRequest;

@RestController
@RequestMapping(ApiConstants.V1_API_PATH + "customer")
public class CustomerController extends AbstractAppsController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    @InfoLogging
    @ValidateBody
    public AddCustomerRestResponse add(@RequestBody CustomerRestRequest request) {
        BaseApplication application = getLoggedApplication();

        CustomerServiceRequest serviceRequest = dtoMapper.map(request, CustomerServiceRequest.class);
        AddressServiceRequest serviceAddress = dtoMapper.map(request.getBillingAddress(), AddressServiceRequest.class);

        BaseCustomer customer = customerService.add(application.getDealer(), serviceRequest, serviceAddress);
        return new AddCustomerRestResponse(getSuccessResultCode(), customer.getToken());
    }

    @GetMapping("/{token}")
    @InfoLogging
    public GetCustomerRestResponse get(@PathVariable String token) {
        BaseApplication application = getLoggedApplication();

        BaseCustomer customer = customerService.getAndLoadBillingAddress(application.getDealer(), token);
        return new GetCustomerRestResponse(getSuccessResultCode(), dtoMapper, customer);
    }

    @PutMapping("/{token}")
    @InfoLogging
    @ValidateBody
    public RestResponseDTO update(@PathVariable String token, @RequestBody CustomerRestRequest request) {
        BaseApplication application = getLoggedApplication();

        CustomerServiceRequest serviceRequest = dtoMapper.map(request, CustomerServiceRequest.class);
        AddressServiceRequest serviceAddress = dtoMapper.map(request.getBillingAddress(), AddressServiceRequest.class);

        customerService.update(application.getDealer(), token, serviceRequest, serviceAddress);
        return createSuccessResponse();
    }

    @DeleteMapping("/{token}")
    @InfoLogging
    public RestResponseDTO delete(@PathVariable String token) {
        BaseApplication application = getLoggedApplication();

        customerService.delete(application.getDealer(), token);
        return createSuccessResponse();
    }

}
