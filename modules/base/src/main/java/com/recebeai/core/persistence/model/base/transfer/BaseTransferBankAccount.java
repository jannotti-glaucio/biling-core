package tech.jannotti.billing.core.persistence.model.base.transfer;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankAccount;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "transfer_bank_account")
public class BaseTransferBankAccount extends BaseTransfer {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "source_bank_account_id")
    private BaseBankAccount sourceBankAccount;

    @ManyToOne
    @JoinColumn(name = "destination_bank_account_id")
    private BaseBankAccount destinationBankAccount;

}
