package tech.jannotti.billing.core.api.web.controllers.dealer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.api.web.controllers.AbstractWebController;
import tech.jannotti.billing.core.api.web.controllers.dto.request.application.ApplicationRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.response.application.AddApplicationRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.application.FindApplicationsRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.application.GenerateApplicationSecretRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.application.GetApplicationRestResponse;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.persistence.model.base.BaseApplication;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.rest.validation.ValidateBody;
import tech.jannotti.billing.core.rest.validation.ValidateParameters;
import tech.jannotti.billing.core.services.ApplicationService;
import tech.jannotti.billing.core.services.dto.request.ApplicationServiceRequest;

@RestController("dealer.applicationController")
@RequestMapping(ApiConstants.V1_API_PATH + "dealer/application")
public class ApplicationController extends AbstractWebController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/")
    @InfoLogging
    @ValidateParameters
    public FindApplicationsRestResponse find() {
        BaseDealer dealer = getLoggedDealer();

        List<BaseApplication> applications = applicationService.find(dealer);

        BaseResultCode resultCode = getQueryResultCode(applications);
        return new FindApplicationsRestResponse(resultCode, dtoMapper, applications);
    }

    @GetMapping("/{token}")
    @InfoLogging
    public GetApplicationRestResponse get(@PathVariable String token) {
        BaseDealer dealer = getLoggedDealer();

        BaseApplication application = applicationService.get(dealer, token);
        return new GetApplicationRestResponse(getSuccessResultCode(), dtoMapper, application);
    }

    @PostMapping
    @InfoLogging
    @ValidateBody
    public AddApplicationRestResponse add(@RequestBody ApplicationRestRequest request) {
        BaseDealer dealer = getLoggedDealer();

        ApplicationServiceRequest serviceRequest = dtoMapper.map(request, ApplicationServiceRequest.class);
        BaseApplication application = applicationService.add(dealer, serviceRequest);
        return new AddApplicationRestResponse(getSuccessResultCode(), application.getToken(), application.getClientId(),
            application.getPlainClientSecret());
    }

    @PutMapping("/{token}")
    @InfoLogging
    @ValidateBody
    public RestResponseDTO update(@PathVariable String token, @RequestBody ApplicationRestRequest request) {
        BaseDealer dealer = getLoggedDealer();

        ApplicationServiceRequest serviceRequest = dtoMapper.map(request, ApplicationServiceRequest.class);
        applicationService.update(dealer, token, serviceRequest);
        return createSuccessResponse();
    }

    @PutMapping("/{token}/generateSecret")
    @InfoLogging
    public GenerateApplicationSecretRestResponse generateSecret(@PathVariable String token) {
        BaseDealer dealer = getLoggedDealer();

        BaseApplication application = applicationService.generateSecret(dealer, token);

        return new GenerateApplicationSecretRestResponse(getSuccessResultCode(), application.getClientId(),
            application.getPlainClientSecret());
    }

    @DeleteMapping("/{token}")
    @InfoLogging
    public RestResponseDTO delete(@PathVariable String token) {
        BaseDealer dealer = getLoggedDealer();

        applicationService.delete(dealer, token);
        return createSuccessResponse();
    }

}
