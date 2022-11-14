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
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.api.web.controllers.AbstractWebController;
import tech.jannotti.billing.core.api.web.controllers.dto.request.billingPlan.BillingPlanRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.response.billingplan.AddCompanyBillingPlanRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.company.FindCompanyBillingPlansRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.company.GetCompanyBillingPlanRestResponse;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBillingPlan;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.rest.validation.ValidateBody;
import tech.jannotti.billing.core.services.company.CompanyBillingPlanService;
import tech.jannotti.billing.core.services.dto.request.BillingPlanServiceRequest;

@RestController("company.billingPlanController")
@RequestMapping(ApiConstants.V1_API_PATH + "company/billingPlan")
public class BillingPlanController extends AbstractWebController {

    @Autowired
    private CompanyBillingPlanService companyBillingPlanService;

    @GetMapping("/")
    @InfoLogging
    public FindCompanyBillingPlansRestResponse find() {
        BaseCompany company = getLoggedCompany();

        List<BaseCompanyBillingPlan> billingPlans = companyBillingPlanService.find(company);
        return new FindCompanyBillingPlansRestResponse(getSuccessResultCode(), dtoMapper, billingPlans);
    }

    @GetMapping("/{token}")
    @InfoLogging
    public GetCompanyBillingPlanRestResponse get(@PathVariable String token) {
        BaseCompany company = getLoggedCompany();

        BaseCompanyBillingPlan billingPlan = companyBillingPlanService.get(company, token);
        return new GetCompanyBillingPlanRestResponse(getSuccessResultCode(), dtoMapper, billingPlan);
    }

    @PostMapping("/")
    @InfoLogging
    @ValidateBody
    public AddCompanyBillingPlanRestResponse add(@RequestBody BillingPlanRestRequest request) {

        BaseCompany company = getLoggedCompany();
        BillingPlanServiceRequest serviceRequest = dtoMapper.map(request, BillingPlanServiceRequest.class);

        BaseCompanyBillingPlan billingPlan = companyBillingPlanService.add(company, serviceRequest);
        return new AddCompanyBillingPlanRestResponse(getSuccessResultCode(), billingPlan.getToken());
    }

    @PutMapping("/{token}")
    @InfoLogging
    @ValidateBody
    public RestResponseDTO update(@PathVariable String token, @RequestBody BillingPlanServiceRequest request) {

        BaseCompany company = getLoggedCompany();
        BillingPlanServiceRequest serviceRequest = dtoMapper.map(request, BillingPlanServiceRequest.class);

        companyBillingPlanService.update(company, token, serviceRequest);
        return createSuccessResponse();
    }

    @DeleteMapping("/{token}")
    @InfoLogging
    public RestResponseDTO delete(@PathVariable String token) {
        BaseCompany company = getLoggedCompany();

        companyBillingPlanService.delete(company, token);
        return createSuccessResponse();
    }

}
