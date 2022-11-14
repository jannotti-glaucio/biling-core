package tech.jannotti.billing.core.api.web.controllers.dto.response.invoice;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.invoice.InvoiceDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;

@Getter
public class GetInvoiceRestResponse extends RestResponseDTO {

    private InvoiceDTO invoice;

    public GetInvoiceRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, BaseInvoice invoice) {
        super(resultCode);
        this.invoice = dtoMapper.map(invoice, InvoiceDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("invoice", invoice);
    }

}
