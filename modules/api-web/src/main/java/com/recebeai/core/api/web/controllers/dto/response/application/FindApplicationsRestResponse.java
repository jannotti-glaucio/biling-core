package tech.jannotti.billing.core.api.web.controllers.dto.response.application;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.ApplicationDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.BaseApplication;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindApplicationsRestResponse extends RestResponseDTO {

    private List<ApplicationDTO> applications;

    public FindApplicationsRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, List<BaseApplication> applications) {
        super(resultCode);
        this.applications = dtoMapper.mapList(applications, ApplicationDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("applications.size", CollectionUtils.size(applications));
    }

}