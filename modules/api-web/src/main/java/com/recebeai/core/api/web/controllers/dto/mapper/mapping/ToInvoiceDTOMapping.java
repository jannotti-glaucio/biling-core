package tech.jannotti.billing.core.api.web.controllers.dto.mapper.mapping;

import org.modelmapper.PropertyMap;

import tech.jannotti.billing.core.api.web.controllers.dto.model.invoice.InvoiceDTO;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;

public class ToInvoiceDTOMapping extends PropertyMap<BaseInvoice, InvoiceDTO> {

    protected void configure() {
        map().setInstalment(source.getFullInstalment());
        map().setNetAmount(source.getNetAmount());
    }
}
