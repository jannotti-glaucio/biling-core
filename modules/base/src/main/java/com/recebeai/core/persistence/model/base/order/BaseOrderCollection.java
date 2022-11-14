package tech.jannotti.billing.core.persistence.model.base.order;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.persistence.enums.CollectionStatusEnum;
import tech.jannotti.billing.core.persistence.enums.converters.CollectionStatusConverter;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "order_collection")
public class BaseOrderCollection extends BaseOrder {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private BaseCustomer customer;

    @Column(name = "document_number")
    private Long documentNumber;

    @Column(name = "instalments")
    private Integer instalments;

    @Column(name = "status")
    @Convert(converter = CollectionStatusConverter.class)
    private CollectionStatusEnum status;

    @Transient
    private Integer totalAmount;

    @Transient
    private Integer paidAmount;

    @Transient
    private Integer pendingAmount;

    @Transient
    private Integer expiredInvoices;

    @Transient
    private Integer paidInvoices;

    @Transient
    private Integer canceledInvoices;

}
