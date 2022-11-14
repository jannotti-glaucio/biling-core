package tech.jannotti.billing.core.api.web.controllers.dto.model.order;

import java.util.List;

import tech.jannotti.billing.core.api.web.controllers.dto.model.customer.CustomerShortDTO;
import tech.jannotti.billing.core.api.web.controllers.dto.model.invoice.InvoiceShortDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.rest.controllers.dto.model.AbstractModelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSubscriptionDTO extends AbstractModelDTO {

    private String token;
    private CustomerShortDTO customer;
    private String description;
    private Long documentNumber;
    private String frequencyType;
    private Integer expirationDay;
    private String startDate;
    private String endDate;
    private Integer amount;
    private String status;

    private Integer createdInvoices;
    private Integer expiredInvoices;
    private Integer paidInvoices;
    private Integer canceledInvoices;

    private List<InvoiceShortDTO> invoices;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("token", token)
            .add("customer", customer)
            .add("description", description)
            .add("documentNumber", documentNumber)
            .add("frequencyType", frequencyType)
            .add("expirationDay", expirationDay)
            .add("startDate", startDate)
            .add("endDate", endDate)
            .add("amount", amount)
            .add("status", status)

            .add("createdInvoices", createdInvoices)
            .add("expiredInvoices", expiredInvoices)
            .add("paidInvoices", paidInvoices)
            .add("canceledInvoices", canceledInvoices);
    }

}
