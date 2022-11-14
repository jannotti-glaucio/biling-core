package tech.jannotti.billing.core.banking.bb.persistence.model;

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
@Table(schema = "banking_bb", name = "billet_registry_request")
public class BancoBrasilBilletRegistryRequest extends AbstractModel {
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

    @Column(name = "numero_convenio")
    private Integer numeroConvenio;

    @Column(name = "numero_carteira")
    private Integer numeroCarteira;

    @Column(name = "numero_variacao_carteira")
    private Integer numeroVariacaoCarteira;

    @Column(name = "data_emissao_titulo")
    private LocalDate dataEmissaoTitulo;

    @Column(name = "data_vencimento_titulo")
    private LocalDate dataVencimentoTitulo;

    @Column(name = "valor_original_titulo")
    private Integer valorOriginalTitulo;

    @Column(name = "codigo_tipo_juro_mora")
    private Integer codigoTipoJuroMora;

    @Column(name = "percentual_juro_mora_titulo")
    private Integer percentualJuroMoraTitulo;

    @Column(name = "codigo_tipo_multa")
    private Integer codigoTipoMulta;

    @Column(name = "data_multa_titulo")
    private LocalDate dataMultaTitulo;

    @Column(name = "percentual_multa_titulo")
    private Integer percentualMultaTitulo;

    @Column(name = "codigo_tipo_titulo")
    private Integer codigoTipoTitulo;

    @Column(name = "texto_numero_titulo_beneficiario")
    private Long textoNumeroTituloBeneficiario;

    @Column(name = "texto_numero_titulo_cliente")
    private String textoNumeroTituloCliente;

    @Column(name = "codigo_tipo_inscricao_pagador")
    private Integer codigoTipoInscricaoPagador;

    @Column(name = "numero_inscricao_pagador")
    private Long numeroInscricaoPagador;

}
