package tech.jannotti.billing.core.persistence.model.base.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "company_user")
public class BaseCompanyUser extends BaseUser {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private BaseCompany company;

    @Column(name = "market_withdraw_notifications")
    private boolean marketWithdrawNotifications;

}
