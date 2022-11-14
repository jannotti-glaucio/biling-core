package tech.jannotti.billing.core.api.web.controllers.dto.model;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HelpVideoDTO extends AbstractModelDTO {

    private String url;
    private String title;
    private String description;
    private String category;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("url", url)
            .add("title", title)
            .add("description", description)
            .add("category", category);
    }

}
