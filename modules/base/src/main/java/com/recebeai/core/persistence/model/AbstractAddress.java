package tech.jannotti.billing.core.persistence.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.StringUtils;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.enums.AddressTypeEnum;
import tech.jannotti.billing.core.persistence.enums.EntityChildStatusEnum;
import tech.jannotti.billing.core.persistence.enums.converters.AddressTypeConverter;
import tech.jannotti.billing.core.persistence.enums.converters.EntityChildStatusConverter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractAddress extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "token")
    protected String token;

    @Column(name = "address_type")
    @Convert(converter = AddressTypeConverter.class)
    protected AddressTypeEnum addressType;

    @Column(name = "billing_address")
    protected boolean billingAddress;

    @Column(name = "street")
    protected String street;

    @Column(name = "number")
    protected String number;

    @Column(name = "complement")
    protected String complement;

    @Column(name = "district")
    protected String district;

    @Column(name = "zip_code")
    protected String zipCode;

    @Column(name = "city")
    protected String city;

    @Column(name = "state")
    protected String state;
    
    @Column(name = "status")
    @Convert(converter = EntityChildStatusConverter.class)
    protected EntityChildStatusEnum status;

    public String getFullStreet() {
        return street
            + (StringUtils.isNotBlank(number) ? ", " + number : "")
            + (StringUtils.isNotBlank(complement) ? ", " + complement : "");
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id)
            .add("token", token)
            .add("addressType", addressType);
    }

}
