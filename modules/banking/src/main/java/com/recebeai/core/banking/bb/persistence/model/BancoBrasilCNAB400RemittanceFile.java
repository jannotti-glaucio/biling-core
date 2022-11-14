package tech.jannotti.billing.core.banking.bb.persistence.model;

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
@Table(schema = "banking_bb", name = "cnab400_remittance_file")
public class BancoBrasilCNAB400RemittanceFile extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_bank_account_id")
    private BancoBrasilCompanyBankAccount companyBankAccount;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Transient
    private String nomeCedente;

    @Transient
    private Integer tipoInscricaoCedente;

    @Transient
    private String numeroInscricaoCedente;

    @Column(name = "numero_sequencial_remessa")
    private Long numeroSequencialRemessa;

    @Transient
    private List<BancoBrasilCNAB400RemittanceDetail> details;

    public BancoBrasilCNAB400RemittanceFile(BancoBrasilCompanyBankAccount companyBankAccount, LocalDateTime creationDate) {
        this.companyBankAccount = companyBankAccount;
        this.creationDate = creationDate;
    }

}
