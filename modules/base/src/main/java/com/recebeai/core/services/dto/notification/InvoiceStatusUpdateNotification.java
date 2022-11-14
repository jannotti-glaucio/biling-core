package tech.jannotti.billing.core.services.dto.notification;

import java.time.LocalDate;

import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.constants.enums.NotificationTypeConstants;
import tech.jannotti.billing.core.persistence.enums.InvoiceStatusEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceStatusUpdateNotification extends AbstractNotificationDTO {

    private String invoiceToken;
    private Long documentNumber;
    private String statusUpdateDate;
    private InvoiceStatusEnum currentStatus;

    public InvoiceStatusUpdateNotification(String invoiceToken, Long documentNumber, LocalDate statusUpdateDate,
        InvoiceStatusEnum currentStatus) {
        super(NotificationTypeConstants.INVOICE_STATUS_UPDATE);

        this.invoiceToken = invoiceToken;
        this.documentNumber = documentNumber;
        this.statusUpdateDate = DateTimeHelper.formatToIsoDate(statusUpdateDate);
        this.currentStatus = currentStatus;
    }

    @Override
    public String toString() {
        return super.buildToString()
            .add("notificationType", notificationType)
            .add("invoiceToken", invoiceToken)
            .add("documentNumber", documentNumber)
            .add("statusUpdateDate", statusUpdateDate)
            .add("currentStatus", currentStatus)
            .toString();
    }

}
