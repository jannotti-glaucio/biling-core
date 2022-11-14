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
import tech.jannotti.billing.core.persistence.enums.BankDischargeOperationEnum;
import tech.jannotti.billing.core.persistence.enums.converters.BankDischargeOperationConverter;
import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePayment;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.persistence.model.base.transfer.BaseTransfer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "bank_discharge")
public class BaseBankDischarge extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BaseBankAccount bankAccount;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private BasePayment payment;

    @ManyToOne
    @JoinColumn(name = "transfer_id")
    private BaseTransfer transfer;

    @ManyToOne
    @JoinColumn(name = "bank_remittance_id")
    private BaseBankRemittance bankRemittance;

    @Column(name = "operation")
    @Convert(converter = BankDischargeOperationConverter.class)
    private BankDischargeOperationEnum operation;

    @Column(name = "processing_date")
    private LocalDateTime processingDate;

    @ManyToOne
    @JoinColumn(name = "result_code_id")
    private BaseResultCode resultCode;

    public BaseBankDischarge(BasePayment payment, BaseBankAccount bankAccount, BaseBankRemittance bankRemittance,
        BankDischargeOperationEnum operation, LocalDateTime processingDate, BaseResultCode resultCode) {
        this.payment = payment;
        this.bankAccount = bankAccount;
        this.bankRemittance = bankRemittance;
        this.operation = operation;
        this.processingDate = processingDate;
        this.resultCode = resultCode;
    }

    public BaseBankDischarge(BasePayment payment, BaseBankAccount bankAccount, BankDischargeOperationEnum operation,
        LocalDateTime processingDate, BaseResultCode resultCode) {
        this.payment = payment;
        this.bankAccount = bankAccount;
        this.operation = operation;
        this.processingDate = processingDate;
        this.resultCode = resultCode;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id)
            .add("operation", operation);
    }

}
