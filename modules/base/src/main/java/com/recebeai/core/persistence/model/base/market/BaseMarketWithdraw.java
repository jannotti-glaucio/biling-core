package tech.jannotti.billing.core.persistence.model.base.market;

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
import tech.jannotti.billing.core.persistence.enums.MarketWithdrawStatusEnum;
import tech.jannotti.billing.core.persistence.enums.converters.MarketWithdrawStatusConverter;
import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealerBankAccount;
import tech.jannotti.billing.core.persistence.model.base.user.BaseUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "market_withdraw")
public class BaseMarketWithdraw extends AbstractModel {
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

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "withdraw_fee")
    private Integer withdrawFee;

    @ManyToOne
    @JoinColumn(name = "dealer_bank_account_id")
    private BaseDealerBankAccount bankAccount;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @ManyToOne
    @JoinColumn(name = "requester_user_id")
    private BaseUser requesterUser;

    @Column(name = "status")
    @Convert(converter = MarketWithdrawStatusConverter.class)
    private MarketWithdrawStatusEnum status;

    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    @ManyToOne
    @JoinColumn(name = "reviewer_user_id")
    private BaseUser reviewerUser;

    @Column(name = "deny_reason")
    private String denyReason;

    @Column(name = "cancelation_date")
    private LocalDateTime cancelationDate;

    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    public int getFees() {
        return withdrawFee;
    }

    public int getNetAmount() {
        return amount - getFees();
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id)
            .add("token", token);
    }

}
