package tech.jannotti.billing.core.api.web.controllers.dto.response.banking;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.BankDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBank;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindBankRestResponse extends RestResponseDTO {

    private List<BankDTO> banks;

    public FindBankRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, List<? extends BaseBank> banks) {
        super(resultCode);
        this.banks = dtoMapper.mapList(banks, BankDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("banks.size", CollectionUtils.size(banks));
    }

}
