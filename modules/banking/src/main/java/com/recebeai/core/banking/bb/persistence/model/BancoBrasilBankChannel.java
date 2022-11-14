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
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankChannel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "banking_bb", name = "bank_channel")
public class BancoBrasilBankChannel extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "base_bank_channel_id")
    private BaseBankChannel baseBankChannel;

    @Column(name = "oauth_url")
    private String oauthUrl;

    @Column(name = "registro_boleto_url")
    private String registroBoletoUrl;

    @Column(name = "request_timeout")
    private Integer requestTimeout;

    @Column(name = "validate_ssl")
    private boolean validateSSL;

    @Column(name = "tipo_operacao_remessa")
    private String tipoOperacaoRemessa;

}
