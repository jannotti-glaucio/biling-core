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
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "banking_bb", name = "translate_billet_registry_erro")
public class BancoBrasilTranslateBilletRegistryErro extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "mensagem")
    private String mensagem;

    @ManyToOne
    @JoinColumn(name = "base_result_code_id")
    private BaseResultCode baseResultCode;

}
