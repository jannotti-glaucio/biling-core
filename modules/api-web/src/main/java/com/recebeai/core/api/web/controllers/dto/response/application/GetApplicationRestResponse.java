package tech.jannotti.billing.core.api.web.controllers.dto.response.application;

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
public class GetApplicationRestResponse extends RestResponseDTO {

    private ApplicationDTO application;

    public GetApplicationRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, BaseApplication application) {
        super(resultCode);
        this.application = dtoMapper.map(application, ApplicationDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("application", application);
    }

}