package tech.jannotti.billing.core.persistence.model.base.alert;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketWithdraw;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "market_withdraw_alert")
public class BaseWithdrawAlert extends BaseAlert {
    private static final long serialVersionUID = 1L;

    @OneToOne
    @JoinColumn(name = "market_withdraw_id")
    private BaseMarketWithdraw withdraw;

}
