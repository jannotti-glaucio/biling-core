package tech.jannotti.billing.core.persistence.repository.base.market;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketStatementType;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface MarketStatementTypeRepository extends AbstractRepository<BaseMarketStatementType, Integer> {

    // TODO Jogar essa consulta em cache

    public BaseMarketStatementType getByCode(String code);

}
