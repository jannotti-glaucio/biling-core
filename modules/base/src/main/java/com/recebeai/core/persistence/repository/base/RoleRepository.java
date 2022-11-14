package tech.jannotti.billing.core.persistence.repository.base;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.BaseRole;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface RoleRepository extends AbstractRepository<BaseRole, Integer> {

    public BaseRole getByCode(String code);

}
