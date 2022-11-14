package tech.jannotti.billing.core.persistence.model.base.bank;

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

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.enums.BankRemittanceDeliveryModeEnum;
import tech.jannotti.billing.core.persistence.enums.BankRemittanceOperationEnum;
import tech.jannotti.billing.core.persistence.enums.BankRemittanceSourceEnum;
import tech.jannotti.billing.core.persistence.enums.BankRemittanceStatusEnum;
import tech.jannotti.billing.core.persistence.enums.converters.BankRemittanceDeliveryModeConverter;
import tech.jannotti.billing.core.persistence.enums.converters.BankRemittanceOperationConverter;
import tech.jannotti.billing.core.persistence.enums.converters.BankRemittanceSourceConverter;
import tech.jannotti.billing.core.persistence.enums.converters.BankRemittanceStatusConverter;
import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePayment;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.persistence.model.base.transfer.BaseTransfer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "bank_remittance")
public class BaseBankRemittance extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BaseBankAccount bankAccount;

    @Column(name = "source")
    @Convert(converter = BankRemittanceSourceConverter.class)
    private BankRemittanceSourceEnum source;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private BasePayment payment;

    @ManyToOne
    @JoinColumn(name = "transfer_id")
    private BaseTransfer transfer;

    @Column(name = "operation")
    @Convert(converter = BankRemittanceOperationConverter.class)
    private BankRemittanceOperationEnum operation;

    @Column(name = "delivery_mode")
    @Convert(converter = BankRemittanceDeliveryModeConverter.class)
    private BankRemittanceDeliveryModeEnum deliveryMode;

    @Column(name = "status")
    @Convert(converter = BankRemittanceStatusConverter.class)
    private BankRemittanceStatusEnum status;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "processing_date")
    private LocalDateTime processingDate;

    @Column(name = "cancelation_date")
    private LocalDateTime cancelationDate;

    @ManyToOne
    @JoinColumn(name = "result_code_id")
    private BaseResultCode resultCode;

    @Column(name = "exception_message")
    private String exceptionMessage;

    public BasePaymentBankBillet getBankBillet() {
        return (BasePaymentBankBillet) payment;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id)
            .add("source", source)
            .add("operation", operation);
    }

}
