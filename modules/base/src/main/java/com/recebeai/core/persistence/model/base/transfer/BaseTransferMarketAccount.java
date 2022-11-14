package tech.jannotti.billing.core.persistence.model.base.transfer;

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
@Table(schema = "base", name = "transfer_market_account")
public class BaseTransferMarketAccount extends BaseTransfer {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "source_market_account_id")
    private BaseMarketAccount sourceMarketAccount;

    @ManyToOne
    @JoinColumn(name = "destination_market_account_id")
    private BaseMarketAccount destinationMarketAccount;

}
