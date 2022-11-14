package tech.jannotti.billing.core.api.web.controllers.dto.response.help;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.HelpVideoDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.help.BaseHelpVideo;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindHelpVideosRestResponse extends RestResponseDTO {

    private List<HelpVideoDTO> videos;

    public FindHelpVideosRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, List<BaseHelpVideo> videos) {
        super(resultCode);
        this.videos = dtoMapper.mapList(videos, HelpVideoDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("videos.size", CollectionUtils.size(videos));
    }

}
