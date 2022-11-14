package tech.jannotti.billing.core.persistence.model.base.dealer;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketAccount;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "dealer_market_account")
public class BaseDealerMarketAccount extends BaseMarketAccount {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "dealer_id")
    private BaseDealer dealer;

}
