package tech.jannotti.billing.core.persistence.repository.base.payment;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import tech.jannotti.billing.core.persistence.enums.PaymentStatusEnum;
import tech.jannotti.billing.core.persistence.model.AbstractModel;

@NoRepositoryBean
public interface AbstractPaymentRepository<T extends AbstractModel> extends JpaRepository<T, Long> {

    @Modifying
    @Query("UPDATE BasePayment SET status=:status WHERE id=:id")
    public void updateStatusById(@Param("id") long id, @Param("status") PaymentStatusEnum status);

    @Modifying
    @Query("UPDATE BasePayment SET status=:status, cancelationRequestDate=:cancelationRequestDate WHERE id=:id")
    public void updateStatusAndCancelationRequestDateById(@Param("id") long id, @Param("status") PaymentStatusEnum status,
        @Param("cancelationRequestDate") LocalDateTime cancelationRequestDate);

    @Modifying
    @Query("UPDATE BasePayment SET status=:status, cancelationDate=:cancelationDate WHERE id=:id")
    public void updateStatusAndCancelationDateById(@Param("id") long id, @Param("status") PaymentStatusEnum status,
        @Param("cancelationDate") LocalDateTime cancelationDate);
}
