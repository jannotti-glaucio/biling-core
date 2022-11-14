package tech.jannotti.billing.core.banking.santander.persistence.model;

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
@Table(schema = "banking_santander", name = "cnab400_discharge_detail")
public class SantanderCNAB400DischargeDetail extends AbstractModel {
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
    private SantanderCNAB400DischargeFile dischargeFile;

    @Column(name = "receive_date")
    private LocalDateTime receiveDate;

    @Column(name = "status")
    @Convert(converter = BankDischargeStatusConverter.class)
    private BankDischargeStatusEnum status;

    @Column(name = "processing_date")
    private LocalDateTime processingDate;

    @Column(name = "nosso_numero")
    private Long nossoNumero;

    @Column(name = "seu_numero")
    private Long seuNumero;

    @Column(name = "data_do_credito")
    private LocalDate dataDoCredito;

    @Column(name = "codigo_do_banco_cobrador")
    private String codigoDoBancoCobrador;

    @Column(name = "agencia_recebedora")
    private String agenciaRecebedora;

    @Column(name = "valor_do_titulo")
    private Integer valorDoTitulo;

    @Column(name = "valor_da_tarifa_cobrada")
    private Integer valorDaTarifaCobrada;

    @Column(name = "valor_dos_juros_de_atraso")
    private Integer valorDosJurosDeAtraso;

    @Column(name = "valor_total_recebido")
    private Integer valorTotalRecebido;

    @Column(name = "valor_dos_juros_de_mora")
    private Integer valorDosJurosDeMora;

    @Column(name = "codigo_de_ocorrencia")
    private Integer codigoDeOcorrencia;

    @Column(name = "data_da_ocorrencia")
    private LocalDate dataDaOcorrencia;

    @Column(name = "codigo_original_da_remessa")
    private Integer codigoOriginalDaRemessa;

    @Column(name = "codigo_do_primeiro_erro")
    private String codigoDoPrimeiroErro;

    @Column(name = "codigo_do_segundo_erro")
    private String codigoDoSegundoErro;

    @Column(name = "codigo_do_terceiro_erro")
    private String codigoDoTerceiroErro;

}
