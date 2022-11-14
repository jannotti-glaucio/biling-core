package tech.jannotti.billing.core.persistence.model.base.order;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.enums.OrderTypeEnum;
import tech.jannotti.billing.core.persistence.enums.converters.OrderTypeConverter;
import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = "base", name = "order")
public abstract class BaseOrder extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "token")
    protected String token;

    @Column(name = "description")
    protected String description;

    @Column(name = "order_type")
    @Convert(converter = OrderTypeConverter.class)
    private OrderTypeEnum orderType;

    @Column(name = "creation_date")
    protected LocalDateTime creationDate;

    @Column(name = "cancelation_date")
    protected LocalDateTime cancelationDate;

    @Transient
    private List<BaseInvoice> invoices;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id)
            .add("token", token);
    }

}
