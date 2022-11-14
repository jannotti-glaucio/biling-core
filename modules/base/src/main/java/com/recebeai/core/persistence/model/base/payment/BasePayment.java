package tech.jannotti.billing.core.persistence.model.base.payment;

import java.time.LocalDate;
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
import tech.jannotti.billing.core.persistence.enums.PaymentMethodEnum;
import tech.jannotti.billing.core.persistence.enums.PaymentStatusEnum;
import tech.jannotti.billing.core.persistence.enums.converters.PaymentMethodConverter;
import tech.jannotti.billing.core.persistence.enums.converters.PaymentStatusConverter;
import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = "base", name = "payment")
public abstract class BasePayment extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    protected BaseInvoice invoice;

    @Column(name = "token")
    protected String token;

    @Column(name = "amount")
    protected Integer amount;

    @Column(name = "payment_method")
    @Convert(converter = PaymentMethodConverter.class)
    protected PaymentMethodEnum paymentMethod;

    @Column(name = "status")
    @Convert(converter = PaymentStatusConverter.class)
    protected PaymentStatusEnum status;

    @Column(name = "creation_date")
    protected LocalDateTime creationDate;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "paid_amount")
    protected Integer paidAmount;

    @Column(name = "payment_cost")
    protected Integer paymentCost;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "released_amount")
    protected Integer releasedAmount;

    @Column(name = "company_fee")
    protected Integer companyFee;

    @Column(name = "cancelation_request_date")
    protected LocalDateTime cancelationRequestDate;

    @Column(name = "cancelation_date")
    protected LocalDateTime cancelationDate;

    public int getFees() {
        if (companyFee != null)
            return companyFee;
        else
            return 0;
    }

    public int getNetAmount() {
        if (paidAmount != null)
            return paidAmount - getFees();
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
