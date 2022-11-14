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
@Table(schema = "banking_bb", name = "cnab400_remittance_detail")
public class BancoBrasilCNAB400RemittanceDetail extends AbstractModel {
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
    private BancoBrasilCNAB400RemittanceFile remittanceFile;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "numero_titulo_cedente")
    private Long numeroTituloCedente;

    @Column(name = "nosso_numero")
    private Long nossoNumero;

    @Column(name = "tipo_inscricao_sacado")
    private Integer tipoInscricaoSacado;

    @Column(name = "numero_inscricao_sacado")
    private String numeroInscricaoSacado;

    @Transient
    private String nomeSacado;

    @Transient
    private String enderecoSacado;

    @Transient
    private String cepSacado;

    @Transient
    private String bairroSacado;

    @Transient
    private String cidadeSacado;

    @Transient
    private String ufSacado;

    @Column(name = "numero_convenio_cobranca_cedente")
    private Integer numeroConvenioCobrancaCedente;

    @Column(name = "numero_carteira")
    private Integer numeroCarteira;

    @Column(name = "variacao_carteira")
    private Integer variacaoCarteira;

    @Column(name = "data_vencimento_titulo")
    private LocalDate dataVencimentoTitulo;

    @Column(name = "data_emissao_titulo")
    private LocalDate dataEmissaoTitulo;

    @Column(name = "valor_titulo")
    private Integer valorTitulo;

    @Column(name = "especie_titulo")
    private Integer especieTitulo;

    @Column(name = "comando")
    private Integer comando;

    public BancoBrasilCNAB400RemittanceDetail(BaseBankRemittance baseBankRemittance) {
        this.baseBankRemittance = baseBankRemittance;
    }

}
