package tech.jannotti.billing.core.persistence.repository.base;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface ResultCodeRepository extends AbstractRepository<BaseResultCode, Integer> {

    public BaseResultCode getByKey(String key);

}