package tech.jannotti.billing.core.persistence.model.base.market;

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

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.BaseLanguage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "market_statement_description")
public class BaseMarketStatementDescription extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "market_statement_type_id")
    private BaseMarketStatementType statementType;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private BaseLanguage languague;

    @Column(name = "description")
    private String description;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id);
    }

}