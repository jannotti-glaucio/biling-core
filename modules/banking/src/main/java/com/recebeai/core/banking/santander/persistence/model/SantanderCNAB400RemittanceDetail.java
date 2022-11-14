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
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankRemittance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "banking_santander", name = "cnab400_remittance_detail")
public class SantanderCNAB400RemittanceDetail extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "base_bank_remittance_id")
    private BaseBankRemittance baseBankRemittance;

    @ManyToOne
    @JoinColumn(name = "cnab400_remittance_file_id")
    private SantanderCNAB400RemittanceFile remittanceFile;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "nosso_numero")
    private Long nossoNumero;

    @Column(name = "codigo_da_carteira")
    private Integer codigoDaCarteira;

    @Column(name = "codigo_da_ocorrencia")
    private Integer codigoDaOcorrencia;

    @Column(name = "seu_numero")
    private Long seuNumero;

    @Column(name = "data_de_vencimento")
    private LocalDate dataDeVencimento;

    @Column(name = "valor_do_titulo")
    private Integer valorDoTitulo;

    @Column(name = "especie_de_documento")
    private Integer especieDeDocumento;

    @Column(name = "data_de_emissao")
    private LocalDate dataDeEmissao;

    @Column(name = "tipo_de_inscricao_do_pagador")
    private Integer tipoDeInscricaoDoPagador;

    @Column(name = "numero_de_inscricao_do_pagador")
    private String numeroDeInscricaoDoPagador;

    @Transient
    private String nomeDoPagador;

    @Transient
    private String enderecoDoPagador;

    @Transient
    private String bairroDoPagador;

    @Transient
    private String cepDoPagador;

    @Transient
    private String complementoDoCepDoPagador;

    @Transient
    private String municipioDoPagador;

    @Transient
    private String estadoDoPagador;

    public SantanderCNAB400RemittanceDetail(BaseBankRemittance baseBankRemittance) {
        this.baseBankRemittance = baseBankRemittance;
    }

}
