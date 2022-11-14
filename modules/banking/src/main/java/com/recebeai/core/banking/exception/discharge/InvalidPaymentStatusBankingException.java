package tech.jannotti.billing.core.banking.exception.discharge;

import tech.jannotti.billing.core.persistence.enums.BankDischargeOperationEnum;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePayment;

@SuppressWarnings("serial")
public class InvalidPaymentStatusBankingException extends BankDischargeProcessingException {

    public InvalidPaymentStatusBankingException(BasePayment payment, BankDischargeOperationEnum operation) {
        super("O Pagamento nao estah no status esperado para esse retorno [paymentId=%s, status=%s, operation=%s]",
            payment.getId(), payment.getStatus(), operation);
    }

}
