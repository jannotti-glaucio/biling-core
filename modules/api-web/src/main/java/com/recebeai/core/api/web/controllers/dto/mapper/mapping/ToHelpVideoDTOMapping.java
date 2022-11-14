package tech.jannotti.billing.core.api.web.controllers.dto.mapper.mapping;

import org.modelmapper.PropertyMap;

import tech.jannotti.billing.core.api.web.controllers.dto.model.HelpVideoDTO;
import tech.jannotti.billing.core.persistence.model.base.help.BaseHelpVideo;

public class ToHelpVideoDTOMapping extends PropertyMap<BaseHelpVideo, HelpVideoDTO> {

    protected void configure() {
        map().setCategory(source.getCategory().getName());
    }
}
