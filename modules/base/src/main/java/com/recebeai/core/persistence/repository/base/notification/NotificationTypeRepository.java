package tech.jannotti.billing.core.persistence.repository.base.notification;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.notification.BaseNotificationType;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface NotificationTypeRepository extends AbstractRepository<BaseNotificationType, Integer> {

    public BaseNotificationType getByCode(String code);

}
