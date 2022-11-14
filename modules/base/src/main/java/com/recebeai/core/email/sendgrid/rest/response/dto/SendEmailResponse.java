package tech.jannotti.billing.core.email.sendgrid.rest.response.dto;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;

import lombok.Getter;

@Getter
public class SendEmailResponse extends AbstractRestRequestDTO {

    private List<Error> errors;

    @Getter
    public static class Error extends AbstractRestRequestDTO {

        private String message;
        private String field;
        private String help;
    }

    public Error getError() {
        if (CollectionUtils.isNotEmpty(errors))
            return errors.get(0);
        else
            return null;
    }

}
