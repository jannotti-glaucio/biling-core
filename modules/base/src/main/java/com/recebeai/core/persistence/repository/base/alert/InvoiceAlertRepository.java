package tech.jannotti.billing.core.persistence.repository.base.alert;

import java.util.List;

import tech.jannotti.billing.core.persistence.enums.AlertStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.alert.BaseInvoiceAlert;

public interface InvoiceAlertRepository extends AbstractAlertRepository<BaseInvoiceAlert> {

    public List<BaseInvoiceAlert> findByInvoiceAndStatus(BaseInvoice invoice, AlertStatusEnum status);

}
