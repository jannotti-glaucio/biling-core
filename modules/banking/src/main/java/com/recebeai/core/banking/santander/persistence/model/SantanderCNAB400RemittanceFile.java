package tech.jannotti.billing.core.banking.santander.persistence.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.persistence.model.AbstractModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "banking_santander", name = "cnab400_remittance_file")
public class SantanderCNAB400RemittanceFile extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_bank_account_id")
    private SantanderCompanyBankAccount companyBankAccount;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "codigo_de_transmissao")
    private String codigoDeTransmissao;

    @Transient
    private String nomeDoBeneficiario;

    @Transient
    private Integer tipoDeInscricaoDoBeneficiario;

    @Transient
    private String numeroDeInscricaoDoBeneficiario;

    @Column(name = "data_de_gravacao")
    private LocalDate dataDeGravacao;

    @Transient
    private Integer qtdTotalDeLinhas;

    @Transient
    private Long valorTotalDosTitulos;

    @Transient
    private List<SantanderCNAB400RemittanceDetail> details;

    public SantanderCNAB400RemittanceFile(SantanderCompanyBankAccount companyBankAccount, LocalDateTime creationDate) {
        this.companyBankAccount = companyBankAccount;
        this.creationDate = creationDate;
    }

}
