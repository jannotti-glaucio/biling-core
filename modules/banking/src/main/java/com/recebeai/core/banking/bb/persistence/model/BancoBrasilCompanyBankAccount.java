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
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "banking_bb", name = "company_bank_account")
public class BancoBrasilCompanyBankAccount extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "base_company_bank_account_id")
    private BaseCompanyBankAccount baseCompanyBankAccount;

    @Column(name = "numero_convenio_lider")
    private Integer numeroConvenioLider;

    @ManyToOne
    @JoinColumn(name = "levying_agreement_id_until_expiration")
    private BancoBrasilLevyingAgreement levyingAgreementUntilExpiration;

    @ManyToOne
    @JoinColumn(name = "levying_agreement_id_expirated")
    private BancoBrasilLevyingAgreement levyingAgreementExpirated;

    @Column(name = "especie_titulo_ws")
    private Integer especieTituloWs;

    @Column(name = "especie_titulo_cnab")
    private Integer especieTituloCnab;

    @Column(name = "especie_titulo_boleto")
    private String especieTituloBoleto;

    @Column(name = "chave_usuario_j")
    private String chaveUsuarioJ;

    @Column(name = "token_clientid")
    private String tokenClientId;

    @Column(name = "token_secret")
    private String tokenSecret;

    @Column(name = "remittance_files_dir")
    private String remittanceFilesDir;

    @Column(name = "discharge_files_source_dir")
    private String dischargeFilesSourceDir;

    @Column(name = "discharge_files_processed_dir")
    private String dischargeFilesProcessedDir;

    @Column(name = "discharge_files_rejected_dir")
    private String dischargeFilesRejectedDir;

    public BancoBrasilLevyingAgreement getLevyingAgreement(boolean expiredPayment) {
        if (expiredPayment)
            return levyingAgreementExpirated;
        else
            return levyingAgreementUntilExpiration;
    }

}
