package tech.jannotti.billing.core.api.web.controllers.dto.mapper.mapping;

import org.modelmapper.PropertyMap;

import tech.jannotti.billing.core.api.web.controllers.dto.model.invoice.InvoiceShortDTO;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;

public class ToInvoiceShortDTOMapping extends PropertyMap<BaseInvoice, InvoiceShortDTO> {

    protected void configure() {
        map().setInstalment(source.getFullInstalment());
    }
}
