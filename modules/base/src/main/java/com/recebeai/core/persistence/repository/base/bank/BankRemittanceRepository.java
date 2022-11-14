package tech.jannotti.billing.core.persistence.repository.base.bank;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.BankRemittanceOperationEnum;
import tech.jannotti.billing.core.persistence.enums.BankRemittanceStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankAccount;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankRemittance;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePayment;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface BankRemittanceRepository extends AbstractRepository<BaseBankRemittance, Long> {

    public BaseBankRemittance getFirstByPaymentAndOperationAndStatusOrderByProcessingDateDesc(BasePayment payment,
        BankRemittanceOperationEnum operation, BankRemittanceStatusEnum status);

    public boolean existsByPaymentAndOperationAndStatus(BasePayment payment, BankRemittanceOperationEnum operation,
        BankRemittanceStatusEnum status);

    @Query(value = "SELECT r FROM BaseBankRemittance r WHERE r.bankAccount = :bankAccount AND r.status = :status")
    public List<BaseBankRemittance> findByBankAccountAndStatus(@Param("bankAccount") BaseBankAccount bankAccount,
        @Param("status") BankRemittanceStatusEnum status);

    @Modifying
    @Query("UPDATE BaseBankRemittance SET status=:status, cancelationDate=:cancelationDate WHERE id=:id")
    public void updateStatusAndCancelationDateById(@Param("id") long id, @Param("status") BankRemittanceStatusEnum status,
        @Param("cancelationDate") LocalDateTime cancelationDate);

    @Modifying
    @Query("UPDATE BaseBankRemittance SET status=:status, processingDate=:processingDate WHERE id=:id")
    public void updateStatusAndProcessingDateById(
        @Param("id") long id,
        @Param("status") BankRemittanceStatusEnum status,
        @Param("processingDate") LocalDateTime processingDate);

}
