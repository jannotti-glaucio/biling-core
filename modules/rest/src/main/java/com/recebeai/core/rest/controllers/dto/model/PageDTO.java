package tech.jannotti.billing.core.rest.controllers.dto.model;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageDTO extends AbstractModelDTO {

    private Integer size;
    private Long totalElements;
    private Integer totalPages;
    private Integer number;

    public PageDTO(int size, long totalElements, int totalPages, int number) {
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.number = number;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("size", size)
            .add("totalElements", totalElements)
            .add("totalPages", totalPages)
            .add("number", number);
    }

}