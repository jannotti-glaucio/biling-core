package tech.jannotti.billing.core.persistence.model.base;

import javax.persistence.Column;
import javax.persistence.Convert;
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
import tech.jannotti.billing.core.commons.util.TextFormatterHelper;
import tech.jannotti.billing.core.persistence.enums.PersonTypeEnum;
import tech.jannotti.billing.core.persistence.enums.converters.PersonTypeConverter;
import tech.jannotti.billing.core.persistence.model.AbstractModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "document_type")
public class BaseDocumentType extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private BaseCountry country;

    @Column(name = "person_type")
    @Convert(converter = PersonTypeConverter.class)
    private PersonTypeEnum personType;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "mask")
    private String mask;

    @Column(name = "web_mask")
    private String webMask;

    @Column(name = "validator_path")
    private String validatorPath;

    public String format(String number) {
        return TextFormatterHelper.format(mask, number);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id)
            .add("country", country)
            .add("code", code);
    }

}
