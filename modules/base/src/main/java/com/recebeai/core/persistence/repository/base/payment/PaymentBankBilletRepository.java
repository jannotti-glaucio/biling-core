package tech.jannotti.billing.core.persistence.repository.base.payment;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.PaymentStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;

@Repository
public interface PaymentBankBilletRepository extends AbstractPaymentRepository<BasePaymentBankBillet> {

    public BasePaymentBankBillet findByToken(String token);

    public List<BasePaymentBankBillet> findByCompanyBankAccountAndExpirationDateLessThanAndStatus(
        BaseCompanyBankAccount companyBankAccount, LocalDate expirationDate, PaymentStatusEnum status);

    public BasePaymentBankBillet getByInvoiceAndStatusIn(BaseInvoice invoice, PaymentStatusEnum[] status);

    public BasePaymentBankBillet getByCompanyBankAccountAndOurNumber(BaseCompanyBankAccount companyBankAccount,
        long ourNumber);

    public BasePaymentBankBillet getByCompanyBankAccountAndYourNumber(BaseCompanyBankAccount companyBankAccount,
        long yourNumber);

    @Modifying
    @Query("UPDATE BasePaymentBankBillet SET ourNumber=:ourNumber, yourNumber=:yourNumber WHERE id=:id")
    public void updateOurNumberAndYourNumberById(@Param("id") long id, @Param("ourNumber") long ourNumber,
        @Param("yourNumber") long yourNumber);

    @Modifying
    @Query("UPDATE BasePaymentBankBillet SET registrationCost=:registrationCost WHERE id=:id")
    public void updateRegistrationCostById(@Param("id") long id, @Param("registrationCost") int registrationCost);

}
