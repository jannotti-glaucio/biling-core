package tech.jannotti.billing.core.api.web.controllers.dto.response.bankaccount;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.BankAccountDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankAccount;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindBankAccountsRestResponse extends RestResponseDTO {

    private List<? extends BankAccountDTO> bankAccounts;

    public FindBankAccountsRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper,
        List<? extends BaseBankAccount> bankAccounts) {
        super(resultCode);
        this.bankAccounts = dtoMapper.mapList(bankAccounts, BankAccountDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("bankAccounts.size", CollectionUtils.size(bankAccounts));
    }

}
