package tech.jannotti.billing.core.persistence.model.base.notification;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.enums.NotificationStatusEnum;
import tech.jannotti.billing.core.persistence.enums.converters.NotificationStatusConverter;
import tech.jannotti.billing.core.persistence.model.AbstractModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = "base", name = "notification")
public abstract class BaseNotification extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "notification_type_id")
    private BaseNotificationType notificationType;

    @Column(name = "destination_url")
    private String destinationUrl;

    @Column(name = "message_content")
    private String messageContent;

    @Column(name = "status")
    @Convert(converter = NotificationStatusConverter.class)
    private NotificationStatusEnum status;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id)
            .add("notificationType", notificationType);
    }

}
