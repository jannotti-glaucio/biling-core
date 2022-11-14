package tech.jannotti.billing.core.banking.bb.persistence.repository;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilDocumentType;
import tech.jannotti.billing.core.persistence.model.base.BaseDocumentType;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.bb.documentTypeRepository")
public interface DocumentTypeRepository extends AbstractRepository<BancoBrasilDocumentType, Integer> {

    public BancoBrasilDocumentType getByBaseDocumentType(BaseDocumentType baseDocumentType);

}
