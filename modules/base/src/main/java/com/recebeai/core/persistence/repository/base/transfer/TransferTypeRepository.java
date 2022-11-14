package tech.jannotti.billing.core.persistence.repository.base.transfer;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.transfer.BaseTransferType;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface TransferTypeRepository extends AbstractRepository<BaseTransferType, Integer> {

    public BaseTransferType getByCode(String code);

}
