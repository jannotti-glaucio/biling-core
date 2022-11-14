package tech.jannotti.billing.core.banking.bb.persistence.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.persistence.enums.BankDischargeStatusEnum;
import tech.jannotti.billing.core.persistence.enums.converters.BankDischargeStatusConverter;
import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankDischarge;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "banking_bb", name = "cnab400_discharge_detail")
public class BancoBrasilCNAB400DischargeDetail extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "base_bank_discharge_id")
    private BaseBankDischarge baseBankDischarge;

    @ManyToOne
    @JoinColumn(name = "cnab400_discharge_file_id")
    private BancoBrasilCNAB400DischargeFile dischargeFile;

    @Column(name = "receive_date")
    private LocalDateTime receiveDate;

    @Column(name = "status")
    @Convert(converter = BankDischargeStatusConverter.class)
    private BankDischargeStatusEnum status;

    @Column(name = "processing_date")
    private LocalDateTime processingDate;

    @Column(name = "numero_titulo_cedente")
    private Long numeroTituloCedente;

    @Column(name = "nosso_numero")
    private Long nossoNumero;

    @Column(name = "numero_convenio_cobranca_cedente")
    private Integer numeroConvenioCobrancaCedente;

    @Column(name = "comando")
    private Integer comando;

    @Column(name = "natureza_recebimento")
    private Integer naturezaRecebimento;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Column(name = "valor_titulo")
    private Integer valorTitulo;

    @Column(name = "codigo_banco_recebedor")
    private String codigoBancoRecebedor;

    @Column(name = "prefixo_agencia_recebedora")
    private String prefixoAgenciaRecebedora;

    @Column(name = "dv_prefixo_agencia_recebedora")
    private String dvPrefixoAgenciaRecebedora;

    @Column(name = "data_liquidacao")
    private LocalDate dataLiquidacao;

    @Column(name = "data_credito")
    private LocalDate dataCredito;

    @Column(name = "valor_tarifa")
    private Integer valorTarifa;

    @Column(name = "valor_recebido")
    private Integer valorRecebido;

    @Column(name = "juros_mora")
    private Integer jurosMora;

    @Column(name = "valor_lancamento")
    private Integer valorLancamento;

    @Column(name = "indicativo_debito_credito")
    private Integer indicativoDebitoCredito;

}
