package tech.jannotti.billing.core.persistence.model.base.company;

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
import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.enums.converters.EntityStatusConverter;
import tech.jannotti.billing.core.persistence.model.AbstractModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "company_billing_plan")
public class BaseCompanyBillingPlan extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private BaseCompany company;

    @Column(name = "token")
    private String token;

    @Column(name = "description")
    private String description;

    @Column(name = "paid_bank_billet_fee")
    private Integer paidBankBilletFee;

    @Column(name = "market_withdraw_fee")
    private Integer marketWithdrawFee;

    @Column(name = "status")
    @Convert(converter = EntityStatusConverter.class)
    private EntityStatusEnum status;
    
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "deletion_date")
    private LocalDateTime deletionDate;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id)
            .add("token", token);
    }

}
