package tech.jannotti.billing.core.banking.santander.persistence.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankRemittance;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "banking_santander", name = "billet_ticket_request")
public class SantanderBilletTicketRequest extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "base_payment_bank_billet_id")
    private BasePaymentBankBillet bankBillet;

    @ManyToOne
    @JoinColumn(name = "base_bank_remittance_id")
    private BaseBankRemittance baseBankRemittance;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @Column(name = "cod_convenio")
    private Integer codConvenio;

    @Column(name = "pagador_tipo_documento")
    private Integer pagadorTipoDocumento;

    @Column(name = "pagador_numero_documento")
    private String pagadorNumeroDocumento;

    @Column(name = "titulo_nosso_numero")
    private Long tituloNossoNumero;

    @Column(name = "titulo_seu_numero")
    private Long tituloSeuNumero;

    @Column(name = "titulo_data_vencimento")
    private LocalDate tituloDataVencimento;

    @Column(name = "titulo_data_emissao")
    private LocalDate tituloDataEmissao;

    @Column(name = "titulo_especie")
    private Integer tituloEspecie;

    @Column(name = "titulo_valor_nominal")
    private Integer tituloValorNominal;

    @Column(name = "titulo_pc_multa")
    private Integer tituloPcMulta;

    @Column(name = "titulo_qtd_dias_multa")
    private Integer tituloQtdDiasMulta;

    @Column(name = "titulo_pc_juro")
    private Integer tituloPcJuro;

    @Column(name = "titulo_qtd_dias_baixa")
    private Integer tituloQtdDiasBaixa;

}
