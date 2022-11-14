package tech.jannotti.billing.core.persistence.model.base.customer;

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
@Table(schema = "base", name = "customer_bank_account")
public class BaseCustomerBankAccount extends BaseBankAccount {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private BaseCustomer customer;

}
