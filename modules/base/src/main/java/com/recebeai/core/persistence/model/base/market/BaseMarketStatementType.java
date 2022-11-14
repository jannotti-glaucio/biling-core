package tech.jannotti.billing.core.persistence.model.base.market;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.constants.CoreConstants;
import tech.jannotti.billing.core.persistence.enums.MarketStatementDirectionEnum;
import tech.jannotti.billing.core.persistence.enums.converters.MarketStatementDirectionConverter;
import tech.jannotti.billing.core.persistence.model.AbstractModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "market_statement_type")
public class BaseMarketStatementType extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Transient
    private LogManager logManager = LogFactory.getManager(BaseMarketStatementType.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "direction")
    @Convert(converter = MarketStatementDirectionConverter.class)
    private MarketStatementDirectionEnum direction;

    @OneToMany(mappedBy = "statementType")
    private List<BaseMarketStatementDescription> descriptions;

    public String getDescription() {
        return getDescription(CoreConstants.DEFAULT_LANGUAGE_CODE);
    }

    public String getDescription(String languageCode) {

        if (CollectionUtils.isNotEmpty(descriptions)) {
            for (BaseMarketStatementDescription description : descriptions) {
                if (description.getLanguague().getCode().equals(languageCode))
                    return description.getDescription();
            }
        }

        logManager.logWARN("Descricao nao localizada para o idioma [%s]", languageCode);
        return null;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id)
            .add("code", code);
    }

}
