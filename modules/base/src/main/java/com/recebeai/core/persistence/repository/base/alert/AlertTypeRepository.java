package tech.jannotti.billing.core.persistence.repository.base.alert;

import tech.jannotti.billing.core.persistence.model.base.alert.BaseAlertType;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

public interface AlertTypeRepository extends AbstractRepository<BaseAlertType, Integer> {

    public BaseAlertType getByCode(String code);

}
