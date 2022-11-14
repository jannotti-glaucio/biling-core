package tech.jannotti.billing.core.email.sendgrid.rest.request.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendEmailRequest extends AbstractRestRequestDTO {

    @JsonProperty("from")
    private From from = new From();

    @JsonProperty("personalizations")
    private List<Personalization> personalizations = new ArrayList<Personalization>();

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("attachments")
    private List<Attachment> attachments;

    @JsonProperty("template_id")
    private String templateId;

    @Getter
    @Setter
    public static class From {

        @JsonProperty("email")
        private String email;

        @JsonProperty("name")
        private String name;
    }

    @Getter
    @Setter
    public static class Attachment {

        @JsonProperty("content")
        private String content;

        @JsonProperty("filename")
        private String filename;
    }

    @Getter
    @Setter
    public static class Personalization {

        @JsonProperty("to")
        private List<To> tos = new ArrayList<To>();

        @JsonProperty("dynamic_template_data")
        private Map<String, String> dynamicTemplateData;

        public void addTO(String email) {
            To to = new To();
            to.setEmail(email);
            tos.add(to);
        }

        @Getter
        @Setter
        public static class To {

            @JsonProperty("email")
            private String email;

            @JsonProperty("name")
            private String name;
        }
    }

}
