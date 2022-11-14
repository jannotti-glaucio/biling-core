package tech.jannotti.billing.core.api.web.controllers.dto.mapper.mapping;

import org.modelmapper.PropertyMap;

import tech.jannotti.billing.core.api.web.controllers.dto.model.payment.PaymentShortDTO;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePayment;

public class ToPaymentShortDTOMapping extends PropertyMap<BasePayment, PaymentShortDTO> {

    protected void configure() {
        map().setFees(source.getFees());
        map().setNetAmount(source.getNetAmount());
    }
}
