package tech.jannotti.billing.core.services.dto.request.invoice;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.order.BaseOrder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCollectionInvoiceServiceRequest extends AddInvoiceServiceRequest {

    private BaseOrder order;
    private Integer instalment;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("order", order)
            .add("installemnt", instalment);
    }

}
