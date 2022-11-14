package tech.jannotti.billing.core.persistence.repository.base.notification;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.notification.BaseNotifyRequest;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface NotifyRequestRepository extends AbstractRepository<BaseNotifyRequest, Long> {

}
