package tech.jannotti.billing.core.api.web.controllers.dto.response.company;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.company.CompanyBillingPlanDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBillingPlan;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetCompanyBillingPlanRestResponse extends RestResponseDTO {

    private CompanyBillingPlanDTO billingPlan;

    public GetCompanyBillingPlanRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper,
        BaseCompanyBillingPlan billingPlan) {
        super(resultCode);
        this.billingPlan = dtoMapper.map(billingPlan, CompanyBillingPlanDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("billingPlan", billingPlan);
    }

}
