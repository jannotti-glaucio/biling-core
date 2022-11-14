package tech.jannotti.billing.core.services.dto.notification;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.constants.enums.NotificationTypeConstants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractNotificationDTO {

    protected String notificationType;

    public AbstractNotificationDTO(NotificationTypeConstants notificationType) {
        this.notificationType = notificationType.name();
    }

    public ToStringHelper buildToString() {
        return ToStringHelper.build(this)
            .add("notificationType", notificationType);
    }

    @Override
    public String toString() {
        return buildToString().toString();
    }

}
