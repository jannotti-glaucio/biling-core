package tech.jannotti.billing.core.persistence.model.base.order;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.persistence.enums.FrequencyTypeEnum;
import tech.jannotti.billing.core.persistence.enums.PaymentMethodEnum;
import tech.jannotti.billing.core.persistence.enums.SubscriptionStatusEnum;
import tech.jannotti.billing.core.persistence.enums.converters.FrequencyTypeConverter;
import tech.jannotti.billing.core.persistence.enums.converters.PaymentMethodConverter;
import tech.jannotti.billing.core.persistence.enums.converters.SubscriptionStatusConverter;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "order_subscription")
public class BaseOrderSubscription extends BaseOrder {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private BaseCustomer customer;

    @Column(name = "document_number")
    private Long documentNumber;

    @Column(name = "frequency_type")
    @Convert(converter = FrequencyTypeConverter.class)
    private FrequencyTypeEnum frequencyType;

    @Column(name = "expiration_day")
    private Integer expirationDay;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "payment_method")
    @Convert(converter = PaymentMethodConverter.class)
    private PaymentMethodEnum paymentMethod;

    @Column(name = "status")
    @Convert(converter = SubscriptionStatusConverter.class)
    private SubscriptionStatusEnum status;

    @Column(name = "finishiment_date")
    protected LocalDateTime finishimentDate;

    @Transient
    private Integer createdInvoices;

    @Transient
    private Integer expiredInvoices;

    @Transient
    private Integer paidInvoices;

    @Transient
    private Integer canceledInvoices;

}
