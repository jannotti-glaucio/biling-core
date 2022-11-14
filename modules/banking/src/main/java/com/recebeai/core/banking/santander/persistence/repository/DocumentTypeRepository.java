package tech.jannotti.billing.core.banking.santander.persistence.repository;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderDocumentType;
import tech.jannotti.billing.core.persistence.model.base.BaseDocumentType;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.santander.documentTypeRepository")
public interface DocumentTypeRepository extends AbstractRepository<SantanderDocumentType, Integer> {

    public SantanderDocumentType getByBaseDocumentType(BaseDocumentType baseDocumentType);

}
