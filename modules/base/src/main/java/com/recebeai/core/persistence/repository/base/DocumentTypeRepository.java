package tech.jannotti.billing.core.persistence.repository.base;

import java.util.List;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.PersonTypeEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseCountry;
import tech.jannotti.billing.core.persistence.model.base.BaseDocumentType;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface DocumentTypeRepository extends AbstractRepository<BaseDocumentType, Integer> {

    public List<BaseDocumentType> findByCountry(BaseCountry country);

    public BaseDocumentType getByCountryAndPersonType(BaseCountry country, PersonTypeEnum personType);

    public BaseDocumentType getByCode(String code);

    public boolean existsByCode(String code);

}