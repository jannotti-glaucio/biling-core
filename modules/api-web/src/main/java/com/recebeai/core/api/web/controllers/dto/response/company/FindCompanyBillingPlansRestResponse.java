package tech.jannotti.billing.core.api.web.controllers.dto.response.company;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

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
public class FindCompanyBillingPlansRestResponse extends RestResponseDTO {

    private List<CompanyBillingPlanDTO> billingPlans;

    public FindCompanyBillingPlansRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper,
        List<BaseCompanyBillingPlan> billingPlans) {
        super(resultCode);
        this.billingPlans = dtoMapper.mapList(billingPlans, CompanyBillingPlanDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("billingPlans.size", CollectionUtils.size(billingPlans));
    }

}
