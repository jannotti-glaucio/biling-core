package tech.jannotti.billing.core.persistence.model.base.company;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
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

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.enums.converters.EntityStatusConverter;
import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.BaseCountry;
import tech.jannotti.billing.core.persistence.model.base.BaseDocumentType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "company")
public class BaseCompany extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "token")
    private String token;

    @Column(name = "trading_name")
    private String tradingName;

    @Column(name = "corporate_name")
    private String corporateName;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private BaseCountry country;

    @ManyToOne
    @JoinColumn(name = "document_type_id")
    private BaseDocumentType documentType;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "minimum_invoice_amount")
    private Integer minimumInvoiceAmount;

    @Column(name = "maximum_invoice_amount")
    private Integer maximumInvoiceAmount;

    @Column(name = "minimum_market_withdraw_amount")
    private Integer minimumMarketWithdrawAmount;

    @Column(name = "maximum_market_withdraw_amount")
    private Integer maximumMarketWithdrawAmount;

    @Column(name = "status")
    @Convert(converter = EntityStatusConverter.class)
    private EntityStatusEnum status;

    @Transient
    private List<BaseCompanyAddress> addresses;

    public BaseCompanyAddress getBillingAddress() {
        BaseCompanyAddress value = addresses.stream()
            .filter(x -> x.isBillingAddress())
            .findFirst()
            .orElse(null);
        return value;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id)
            .add("token", token);
    }

}
