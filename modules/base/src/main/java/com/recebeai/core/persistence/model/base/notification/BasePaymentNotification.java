package tech.jannotti.billing.core.persistence.model.base.notification;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.persistence.model.base.payment.BasePayment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "payment_notification")
public class BasePaymentNotification extends BaseNotification {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private BasePayment payment;

}
