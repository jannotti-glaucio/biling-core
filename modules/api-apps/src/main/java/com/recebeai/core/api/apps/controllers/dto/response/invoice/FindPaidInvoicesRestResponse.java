package tech.jannotti.billing.core.api.apps.controllers.dto.response.invoice;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import tech.jannotti.billing.core.api.apps.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.apps.controllers.dto.model.invoice.PaidInvoiceDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;

@Getter
public class FindPaidInvoicesRestResponse extends RestResponseDTO {

    private List<PaidInvoiceDTO> invoices;

    public FindPaidInvoicesRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, List<BaseInvoice> invoices) {
        super(resultCode);
        this.invoices = dtoMapper.mapList(invoices, PaidInvoiceDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("invoices.size", CollectionUtils.size(invoices));
    }

}