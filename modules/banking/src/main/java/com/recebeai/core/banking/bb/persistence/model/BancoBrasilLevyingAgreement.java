package tech.jannotti.billing.core.banking.bb.persistence.model;

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

import tech.jannotti.billing.core.persistence.model.AbstractModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "banking_bb", name = "levying_agreement")
public class BancoBrasilLevyingAgreement extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "company_bank_account_id")
    private BancoBrasilCompanyBankAccount companyBankAccount;

    @Column(name = "billet_expired_payment")
    private boolean billetExpiredPayment;

    @Column(name = "numero_convenio")
    private Integer numeroConvenio;

    @Column(name = "numero_carteira")
    private Integer numeroCarteira;

    @Column(name = "variacao_carteira")
    private Integer variacaoCarteira;

}
