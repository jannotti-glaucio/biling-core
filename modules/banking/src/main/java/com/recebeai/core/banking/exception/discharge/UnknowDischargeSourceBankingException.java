package tech.jannotti.billing.core.banking.exception.discharge;

import tech.jannotti.billing.core.persistence.model.base.payment.BasePayment;

@SuppressWarnings("serial")
public class UnknowDischargeSourceBankingException extends BankDischargeProcessingException {

    public UnknowDischargeSourceBankingException(BasePayment payment) {
        super("O Pagamento nao estah em um status que esteja esperando algum retorno bancario [paymentId=%s, status=%s]",
            payment.getId(), payment.getStatus());
    }

}
