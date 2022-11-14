package tech.jannotti.billing.core.api.web.controllers.dto.request.bank;

import javax.validation.Valid;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.rest.AbstractRestRequestDTO;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ParameterLength;
import tech.jannotti.billing.core.validation.extension.groups.AddValidations;
import tech.jannotti.billing.core.validation.extension.groups.UpdateValidations;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountRestRequest extends AbstractRestRequestDTO {

    @NotBlankParameter(groups = { AddValidations.class, UpdateValidations.class })
    @Valid
    protected String bank;

    @NotBlankParameter(groups = { AddValidations.class, UpdateValidations.class })
    @ParameterLength(max = 4)
    protected String agencyNumber;

    @ParameterLength(max = 1)
    protected String agencyCheckDigit;

    @NotBlankParameter(groups = { AddValidations.class, UpdateValidations.class })
    @ParameterLength(max = 7)
    protected String accountNumber;

    @ParameterLength(max = 1)
    protected String accountCheckDigit;

    @NotBlankParameter(groups = { AddValidations.class, UpdateValidations.class })
    @ParameterLength(max = 50)
    protected String description;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("bank", bank)
            .add("agencyNumber", agencyNumber)
            .add("agencyCheckDigit", agencyCheckDigit)
            .add("accountNumber", accountNumber)
            .add("accountCheckDigit", accountCheckDigit)
            .add("description", description);
    }

}
