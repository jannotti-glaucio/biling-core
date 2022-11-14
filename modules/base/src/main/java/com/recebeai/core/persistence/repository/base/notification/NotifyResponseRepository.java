package tech.jannotti.billing.core.persistence.repository.base.notification;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.notification.BaseNotification;
import tech.jannotti.billing.core.persistence.model.base.notification.BaseNotifyResponse;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface NotifyResponseRepository extends AbstractRepository<BaseNotifyResponse, Long> {

    public int countByNotifyRequestNotification(BaseNotification notification);

}
