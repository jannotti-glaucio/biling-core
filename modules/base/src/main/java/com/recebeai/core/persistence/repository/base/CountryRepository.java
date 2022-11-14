package tech.jannotti.billing.core.persistence.repository.base;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.BaseCountry;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface CountryRepository extends AbstractRepository<BaseCountry, Integer> {

    public BaseCountry getByCode(String code);

    public boolean existsByCode(String code);

}