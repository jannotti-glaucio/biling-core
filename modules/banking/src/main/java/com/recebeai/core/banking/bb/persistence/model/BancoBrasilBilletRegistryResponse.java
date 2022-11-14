package tech.jannotti.billing.core.banking.bb.persistence.model;

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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "banking_bb", name = "billet_registry_response")
public class BancoBrasilBilletRegistryResponse extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "billet_registry_request_id")
    private BancoBrasilBilletRegistryRequest billetRegistryRequest;

    @Column(name = "response_date")
    private LocalDateTime responseDate;

    @Column(name = "codigo_retorno_programa")
    private Short codigoRetornoPrograma;

    @Column(name = "nome_programa_erro")
    private String nomeProgramaErro;

    @Column(name = "texto_mensagem_erro")
    private String textoMensagemErro;

    @Column(name = "numero_posicao_erro_programa")
    private Short numeroPosicaoErroPrograma;

    @Column(name = "linha_digitavel")
    private String linhaDigitavel;

    @Column(name = "codigo_barra_numerico")
    private String codigoBarraNumerico;

}
