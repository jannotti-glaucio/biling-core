package tech.jannotti.billing.core.persistence.repository.base;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.BaseCountry;
import tech.jannotti.billing.core.persistence.model.base.BaseLanguage;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface LanguageRepository extends AbstractRepository<BaseLanguage, Integer> {

    public BaseLanguage getByCountry(BaseCountry country);

}
