package tech.jannotti.billing.core.api.web.controllers.dto.response.invoice;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;

@Getter
public class GetInvoiceFeesRestResponse extends RestResponseDTO {

    private Integer fees;
    private Integer netAmount;

    public GetInvoiceFeesRestResponse(BaseResultCode resultCode, int fees, int netAmount) {
        super(resultCode);
        this.fees = fees;
        this.netAmount = netAmount;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("fees", fees)
            .add("netAmount", netAmount);
    }

}
