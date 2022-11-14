package tech.jannotti.billing.core.persistence.repository.base;

import java.util.List;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.BaseCountry;
import tech.jannotti.billing.core.persistence.model.base.BaseState;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface StateRepository extends AbstractRepository<BaseState, Integer> {

    public List<BaseState> findByCountry(BaseCountry country);

    public BaseState getByCountryAndCode(BaseCountry country, String code);

}
