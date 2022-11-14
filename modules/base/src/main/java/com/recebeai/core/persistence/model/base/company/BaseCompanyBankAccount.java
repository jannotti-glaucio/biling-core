package tech.jannotti.billing.core.persistence.model.base.company;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.persistence.model.base.BaseDocumentType;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBank;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankAccount;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankChannel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "company_bank_account")
public class BaseCompanyBankAccount extends BaseBankAccount {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private BaseCompany company;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private BaseBank bank;

    @ManyToOne
    @JoinColumn(name = "bank_channel_id")
    private BaseBankChannel bankChannel;

    @Column(name = "acceptance")
    private boolean acceptance;

    @Column(name = "billet_unpaid_limit")
    private Integer billetUnpaidLimit;

    @Column(name = "registered_billet_fee")
    private Integer registeredBilletFee;

    @Column(name = "paid_billet_fee")
    private Integer paidBilletFee;

    @Column(name = "interbank_transfer_fee")
    private Integer interbankTransferFee;

    public String getBeneficiaryName() {
        return company.getCorporateName();
    }

    public BaseDocumentType getBeneficiaryDocumentType() {
        return company.getDocumentType();
    }

    public String getBeneficiaryDocumentNumber() {
        return company.getDocumentNumber();
    }

}
