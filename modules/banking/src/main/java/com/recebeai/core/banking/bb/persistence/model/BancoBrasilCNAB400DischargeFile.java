package tech.jannotti.billing.core.banking.bb.persistence.model;

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
@Table(schema = "banking_bb", name = "cnab400_discharge_file")
public class BancoBrasilCNAB400DischargeFile extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_bank_account_id")
    private BancoBrasilCompanyBankAccount companyBankAccount;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "receive_date")
    private LocalDateTime receiveDate;

    @Column(name = "data_gravacao")
    private LocalDate dataGravacao;

    @Column(name = "qtd_titulos")
    private Integer qtdTitulos;

    @Column(name = "valor_total")
    private Long valorTotal;

    @Transient
    private List<BancoBrasilCNAB400DischargeDetail> details;

    public BancoBrasilCNAB400DischargeFile(BancoBrasilCompanyBankAccount companyBankAccount, String fileName,
        LocalDateTime receiveDate) {
        this.companyBankAccount = companyBankAccount;
        this.fileName = fileName;
        this.receiveDate = receiveDate;
    }

}
