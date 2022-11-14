package tech.jannotti.billing.core.persistence.model.base.bank;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.enums.EntityChildStatusEnum;
import tech.jannotti.billing.core.persistence.enums.converters.EntityChildStatusConverter;
import tech.jannotti.billing.core.persistence.model.AbstractModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = "base", name = "bank_account")
public abstract class BaseBankAccount extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    protected BaseBank bank;

    @Column(name = "token")
    protected String token;

    @Column(name = "agency_number")
    protected String agencyNumber;

    @Column(name = "agency_check_digit")
    protected String agencyCheckDigit;

    @Column(name = "account_number")
    protected String accountNumber;

    @Column(name = "account_check_digit")
    protected String accountCheckDigit;

    @Column(name = "description")
    protected String description;

    @Column(name = "status")
    @Convert(converter = EntityChildStatusConverter.class)
    protected EntityChildStatusEnum status;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id)
            .add("token", token);
    }

}
