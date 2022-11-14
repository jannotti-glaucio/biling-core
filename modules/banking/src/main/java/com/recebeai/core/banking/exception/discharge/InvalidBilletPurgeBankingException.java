package tech.jannotti.billing.core.banking.exception.discharge;

import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;

@SuppressWarnings("serial")
public class InvalidBilletPurgeBankingException extends BankDischargeProcessingException {

    public InvalidBilletPurgeBankingException(BasePaymentBankBillet bankBillet) {
        super(
            "O Boleto nao foi registrado para baixa automatica apos o vencimento [bankBilletId=%s, expiredPayment=%s, status=%s]",
            bankBillet.getId(), bankBillet.isExpiredPayment(), bankBillet.getStatus());
    }

}
