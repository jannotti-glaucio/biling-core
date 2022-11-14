package tech.jannotti.billing.core.banking.santander.persistence.model;

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
@Table(schema = "banking_santander", name = "billet_registry_response")
public class SantanderBilletRegistryResponse extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "billet_registry_request_id")
    private SantanderBilletRegistryRequest billetRegistryRequest;

    @Column(name = "response_date")
    private LocalDateTime responseDate;

    @Column(name = "situacao")
    private Integer situacao;

    @Column(name = "descricao_erro_codigo")
    private String descricaoErroCodigo;

    @Column(name = "descricao_erro_mensagem")
    private String descricaoErroMensagem;

    @Column(name = "titulo_lin_dig")
    private String tituloLinDig;

}
