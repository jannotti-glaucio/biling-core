package tech.jannotti.billing.core.persistence.model.base;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.enums.InvoiceStatusEnum;
import tech.jannotti.billing.core.persistence.enums.PaymentMethodEnum;
import tech.jannotti.billing.core.persistence.enums.converters.InvoiceStatusConverter;
import tech.jannotti.billing.core.persistence.enums.converters.PaymentMethodConverter;
import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomer;
import tech.jannotti.billing.core.persistence.model.base.order.BaseOrder;
import tech.jannotti.billing.core.persistence.model.base.order.BaseOrderCollection;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "invoice")
public class BaseInvoice extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private BaseCustomer customer;

    @Column(name = "token")
    private String token;

    @Column(name = "description")
    private String description;

    @Column(name = "document_number")
    private Long documentNumber;

    @Column(name = "amount")
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private BaseOrder order;

    @Column(name = "instalment")
    private Integer instalment;

    @Column(name = "reference_date")
    private LocalDate referenceDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "payment_method")
    @Convert(converter = PaymentMethodConverter.class)
    private PaymentMethodEnum paymentMethod;

    @Column(name = "status")
    @Convert(converter = InvoiceStatusConverter.class)
    private InvoiceStatusEnum status;

    @Column(name = "callback_url")
    private String callbackUrl;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "cancelation_date")
    private LocalDateTime cancelationDate;

    @Transient
    private Integer paidAmount;

    @Transient
    private Integer fees;

    @Transient
    private BasePaymentBankBillet bankBillet;

    public BaseOrderCollection getOrderCollection() {
        if ((order != null) && (order instanceof BaseOrderCollection)) {
            return (BaseOrderCollection) order;
        } else
            return null;
    }

    public String getFullInstalment() {
        if (instalment == null)
            return null;

        BaseOrderCollection collection = getOrderCollection();
        if (collection != null)
            return instalment + "/" + collection.getInstalments();
        else
            return null;
    }

    public int getNetAmount() {

        if ((paidAmount != null) && (fees != null))
            return paidAmount - fees;
        else if ((paidAmount != null) && (fees == null))
            return paidAmount;
        else
            return 0;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id)
            .add("token", token);
    }

}
