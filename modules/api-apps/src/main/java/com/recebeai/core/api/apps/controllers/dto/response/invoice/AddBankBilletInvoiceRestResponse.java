package tech.jannotti.billing.core.api.apps.controllers.dto.response.invoice;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;

@Getter
public class AddBankBilletInvoiceRestResponse extends RestResponseDTO {

    private String token;
    public String lineCode;
    private String url;

    public AddBankBilletInvoiceRestResponse(BaseResultCode resultCode, String token, String lineCode, String url) {
        super(resultCode);
        this.token = token;
        this.lineCode = lineCode;
        this.url = url;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("lineCode", lineCode)
            .add("url", url);
    }

}