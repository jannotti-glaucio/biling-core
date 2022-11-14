package tech.jannotti.billing.core.api.web.controllers.dto.model.payment;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentBankBilletShortDTO extends AbstractModelDTO {

    // TODO Criar um padrão de TO simplificado pra entidade payment bank billet
    // TODO Criar um campo pra devolver o banco de onde o boleto vai vir

    private String lineCode;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("lineCode", lineCode);
    }

}
