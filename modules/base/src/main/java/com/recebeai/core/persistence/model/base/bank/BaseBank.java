package tech.jannotti.billing.core.persistence.model.base.bank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.AbstractModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "bank")
public class BaseBank extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "connector_path")
    private String connectorPath;

    @Column(name = "billet_beneficiary_name")
    private String billetBeneficiaryName;

    @Column(name = "billet_demonstratives")
    private String billetDemonstratives;

    @Column(name = "billet_payment_place_payable_until_expiration")
    private String billetPaymentPlacePayableUntilExpiration;

    @Column(name = "billet_payment_place_payable_expirated")
    private String billetPaymentPlacePayableExpirated;

    @Column(name = "billet_instructions_payable_until_expiration")
    private String billetInstructionsPayableUntilExpiration;

    @Column(name = "billet_instructions_payable_expirated")
    private String billetInstructionsPayableExpirated;

    @Column(name = "billet_our_number_digits")
    private Integer billetOurNumberDigits;

    @Column(name = "billet_our_number_print_mask")
    private String billetOurNumberPrintMask;

    public String getBilletPaymentPlace(boolean expiredPayment) {
        if (expiredPayment)
            return billetPaymentPlacePayableExpirated;
        else
            return billetPaymentPlacePayableUntilExpiration;
    }

    public String getBilletInstructions(boolean expiredPayment) {
        if (expiredPayment)
            return billetInstructionsPayableExpirated;
        else
            return billetInstructionsPayableUntilExpiration;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("code", code)
            .add("id", id);
    }

}
