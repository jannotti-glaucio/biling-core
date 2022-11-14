package tech.jannotti.billing.core.persistence.model.base.dealer;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
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
import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.enums.PersonTypeEnum;
import tech.jannotti.billing.core.persistence.enums.converters.EntityStatusConverter;
import tech.jannotti.billing.core.persistence.enums.converters.PersonTypeConverter;
import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.BaseDocumentType;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBillingPlan;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "dealer")
public class BaseDealer extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private BaseCompany company;

    @Column(name = "token")
    private String token;

    @Column(name = "name")
    private String name;

    @Column(name = "person_type")
    @Convert(converter = PersonTypeConverter.class)
    private PersonTypeEnum personType;

    @ManyToOne
    @JoinColumn(name = "document_type_id")
    private BaseDocumentType documentType;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "comments")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "company_bank_account_id")
    private BaseCompanyBankAccount companyBankAccount;

    @ManyToOne
    @JoinColumn(name = "company_billing_plan_id")
    private BaseCompanyBillingPlan billingPlan;

    @Column(name = "bank_billet_instructions")
    private String bankBilletInstructions;

    @Column(name = "bank_billet_expired_payment")
    private boolean bankBilletExpiredPayment;

    @Column(name = "bank_billet_penalty_percent")
    private Integer bankBilletPenaltyPercent;

    @Column(name = "bank_billet_interest_percent")
    private Integer bankBilletInterestPercent;

    @Column(name = "status")
    @Convert(converter = EntityStatusConverter.class)
    private EntityStatusEnum status;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "deletion_date")
    private LocalDateTime deletionDate;

    @Transient
    private List<BaseDealerBankAccount> bankAccounts;

    @Transient
    private List<BaseDealerAddress> addresses;

    public BaseDealerAddress getBillingAddress() {
        BaseDealerAddress value = addresses.stream()
            .filter(x -> x.isBillingAddress())
            .findFirst()
            .orElse(null);
        return value;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id)
            .add("token", token);
    }

}
