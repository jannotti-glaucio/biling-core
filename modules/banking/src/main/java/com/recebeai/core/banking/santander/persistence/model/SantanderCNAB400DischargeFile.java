package tech.jannotti.billing.core.banking.santander.persistence.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
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
@Table(schema = "banking_santander", name = "cnab400_discharge_file")
public class SantanderCNAB400DischargeFile extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_bank_account_id")
    private SantanderCompanyBankAccount companyBankAccount;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "receive_date")
    private LocalDateTime receiveDate;

    @Column(name = "agencia_beneficiario")
    private String agenciaBeneficiario;

    @Column(name = "conta_movimento_beneficiario")
    private String contaMovimentoBeneficiario;

    @Column(name = "data_do_movimento")
    private LocalDate dataDoMovimento;

    @Column(name = "qtd_de_cobrancas_simples")
    private Integer qtdDeCobrancasSimples;

    @Column(name = "valor_de_cobrancas_simples")
    private Long valorDeCobrancasSimples;

    @Transient
    private List<SantanderCNAB400DischargeDetail> details;

    public SantanderCNAB400DischargeFile(SantanderCompanyBankAccount companyBankAccount, String fileName,
        LocalDateTime receiveDate) {
        this.companyBankAccount = companyBankAccount;
        this.fileName = fileName;
        this.receiveDate = receiveDate;
    }

}
