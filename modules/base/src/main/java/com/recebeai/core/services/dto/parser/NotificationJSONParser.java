package tech.jannotti.billing.core.services.dto.parser;

import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.commons.bean.parser.BeanJSONParser;
import tech.jannotti.billing.core.services.dto.notification.InvoiceStatusUpdateNotification;

@Component
public class NotificationJSONParser extends BeanJSONParser {

    public NotificationJSONParser() {
        super(InvoiceStatusUpdateNotification.class);
    }

}
