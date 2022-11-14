package tech.jannotti.billing.core.persistence.repository.base.notification;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.NotificationStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.notification.BaseNotification;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface NotificationRepository extends AbstractRepository<BaseNotification, Long> {

    public List<BaseNotification> findByStatus(NotificationStatusEnum status);

    @Modifying
    @Query("UPDATE BaseNotification SET status=:status, deliveryDate=:deliveryDate WHERE id=:id")
    public void updateStatusAndDeliveryDateById(@Param("id") long id, @Param("status") NotificationStatusEnum status,
        @Param("deliveryDate") LocalDateTime deliveryDate);

    @Modifying
    @Query("UPDATE BaseNotification SET status=:status WHERE id=:id")
    public void updateStatusById(@Param("id") long id, @Param("status") NotificationStatusEnum status);

}
