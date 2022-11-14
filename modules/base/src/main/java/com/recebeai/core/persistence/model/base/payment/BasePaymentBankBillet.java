package tech.jannotti.billing.core.persistence.model.base.payment;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.persistence.model.base.BaseDocumentType;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "payment_bank_billet")
public class BasePaymentBankBillet extends BasePayment {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "company_bank_account_id")
    private BaseCompanyBankAccount companyBankAccount;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "payer_name")
    private String payerName;

    @ManyToOne
    @JoinColumn(name = "payer_document_type_id")
    private BaseDocumentType payerDocumentType;

    @Column(name = "payer_document_number")
    private String payerDocumentNumber;

    @Column(name = "payer_address_street")
    private String payerAddressStreet;

    @Column(name = "payer_address_number")
    private String payerAddressNumber;

    @Column(name = "payer_address_complement")
    private String payerAddressComplement;

    @Column(name = "payer_address_district")
    private String payerAddressDistrict;

    @Column(name = "payer_address_zip_code")
    private String payerAddressZipCode;

    @Column(name = "payer_address_city")
    private String payerAddressCity;

    @Column(name = "payer_address_state")
    private String payerAddressState;

    @Column(name = "our_number")
    private Long ourNumber;

    @Column(name = "your_number")
    private Long yourNumber;

    @Column(name = "document_number")
    private Long documentNumber;

    @Column(name = "expired_payment")
    private boolean expiredPayment;

    @Column(name = "penalty_start_date")
    private LocalDate penaltyStartDate;

    @Column(name = "penalty_percent")
    private Integer penaltyPercent;

    @Column(name = "interest_percent")
    private Integer interestPercent;

    @Column(name = "line_code")
    private String lineCode;

    @Transient
    private String url;

    @Column(name = "registration_cost")
    private Integer registrationCost;

    public String getPayerAddressFullStreet() {
        return payerAddressStreet
            + (StringUtils.isNotBlank(payerAddressNumber) ? ", " + payerAddressNumber : "")
            + (StringUtils.isNotBlank(payerAddressComplement) ? ", " + payerAddressComplement : "");
    }

}
