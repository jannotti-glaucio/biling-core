package tech.jannotti.billing.core.banking.santander.persistence.model;

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
@Table(schema = "banking_santander", name = "company_bank_account")
public class SantanderCompanyBankAccount extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "base_company_bank_account_id")
    private BaseCompanyBankAccount baseCompanyBankAccount;

    @Column(name = "codigo_convenio_cobranca")
    private Integer codigoConvenioCobranca;

    @Column(name = "codigo_estacao_cobranca")
    private String codigoEstacaoCobranca;

    @Column(name = "especie_titulo_xml")
    private Integer especieTituloXml;

    @Column(name = "especie_titulo_boleto")
    private String especieTituloBoleto;

    @Column(name = "especie_titulo_cnab")
    private Integer especieTituloCnab;

    @Column(name = "codigo_carteira_boleto")
    private Integer codigoCarteiraBoleto;

    @Column(name = "sigla_carteira_boleto")
    private String siglaCarteiraBoleto;

    @Column(name = "codigo_carteira_cnab")
    private Integer codigoCarteiraCnab;

    @Column(name = "codigo_transmissao_cnab")
    private String codigoTransmissaoCnab;

    @Column(name = "remittance_files_dir")
    private String remittanceFilesDir;

    @Column(name = "discharge_files_source_dir")
    private String dischargeFilesSourceDir;

    @Column(name = "discharge_files_processed_dir")
    private String dischargeFilesProcessedDir;

    @Column(name = "discharge_files_rejected_dir")
    private String dischargeFilesRejectedDir;

}
