package tech.jannotti.billing.core.persistence.repository.base;

import java.util.List;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.BaseCity;
import tech.jannotti.billing.core.persistence.model.base.BaseState;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface CityRepository extends AbstractRepository<BaseCity, Integer> {

    public List<BaseCity> findByState(BaseState state);

    public BaseCity getByStateAndNameIgnoreCase(BaseState state, String name);
}