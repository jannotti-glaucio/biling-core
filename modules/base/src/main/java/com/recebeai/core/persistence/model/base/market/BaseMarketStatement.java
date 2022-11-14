package tech.jannotti.billing.core.persistence.model.base.market;

import java.time.LocalDate;

import javax.persistence.Column;
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
import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePayment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "market_statement")
public class BaseMarketStatement extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "market_account_id")
    private BaseMarketAccount marketAccount;

    @Column(name = "token")
    private String token;

    @ManyToOne
    @JoinColumn(name = "market_statement_type_id")
    private BaseMarketStatementType type;

    @Column(name = "statement_date")
    private LocalDate statementDate;

    @Column(name = "amount")
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private BasePayment payment;

    @ManyToOne
    @JoinColumn(name = "market_withdraw_id")
    protected BaseMarketWithdraw marketWithdraw;

    @Transient
    private Integer balance;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id)
            .add("token", token);
    }

}
