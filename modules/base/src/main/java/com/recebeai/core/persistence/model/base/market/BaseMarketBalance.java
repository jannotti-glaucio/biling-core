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

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.AbstractModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "market_balance")
public class BaseMarketBalance extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "market_account_id")
    private BaseMarketAccount marketAccount;

    @Column(name = "balance_date")
    private LocalDate balanceDate;

    @Column(name = "debits_summary")
    private Integer debitsSummary;

    @Column(name = "credits_summary")
    private Integer creditsSummary;

    @Column(name = "prior_balance")
    private Integer priorBalance;

    public Integer getCurrentBalance() {
        return priorBalance + creditsSummary - debitsSummary;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id);
    }

}
