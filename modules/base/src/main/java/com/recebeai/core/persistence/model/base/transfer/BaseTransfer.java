package tech.jannotti.billing.core.persistence.model.base.transfer;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.enums.TransferStatusEnum;
import tech.jannotti.billing.core.persistence.enums.converters.TransferStatusConverter;
import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketWithdraw;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = "base", name = "transfer")
public abstract class BaseTransfer extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "token")
    protected String token;

    @ManyToOne
    @JoinColumn(name = "transfer_type_id")
    protected BaseTransferType transferType;

    @Column(name = "amount")
    protected Integer amount;

    @Column(name = "transfer_cost")
    protected Integer transferCost;

    @Column(name = "transfer_date")
    protected LocalDate transferDate;

    @Column(name = "status")
    @Convert(converter = TransferStatusConverter.class)
    protected TransferStatusEnum status;

    @Column(name = "creation_date")
    protected LocalDateTime creationDate;

    @Column(name = "processing_date")
    protected LocalDateTime processingDate;

    @Column(name = "cancelation_date")
    protected LocalDateTime cancelationDate;

    @ManyToOne
    @JoinColumn(name = "market_withdraw_id")
    protected BaseMarketWithdraw marketWithdraw;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id)
            .add("token", token);
    }

}
