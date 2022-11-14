package tech.jannotti.billing.core.api.web.controllers.dto.response.alert;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.alert.InvoiceAlertDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.alert.BaseInvoiceAlert;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;

@Getter
public class FindInvoiceAlertsRestResponse extends RestResponseDTO {

	private List<InvoiceAlertDTO> invoiceAlerts;

    public FindInvoiceAlertsRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, List<BaseInvoiceAlert> baseInvoiceAlerts) {
        super(resultCode);
        this.invoiceAlerts = dtoMapper.mapList(baseInvoiceAlerts, InvoiceAlertDTO.class);
    }
    
    public FindInvoiceAlertsRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, Page<BaseInvoiceAlert> page) {
        super(page, resultCode);
        this.invoiceAlerts = dtoMapper.mapList(page.getContent(), InvoiceAlertDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("invoiceAlerts.size", CollectionUtils.size(invoiceAlerts));
    }
}
