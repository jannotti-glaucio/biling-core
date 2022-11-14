package tech.jannotti.billing.core.persistence.repository.base.market;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import tech.jannotti.billing.core.persistence.model.AbstractModel;

@NoRepositoryBean
public interface AbstractMarketAccountRepository<T extends AbstractModel> extends JpaRepository<T, Long> {

}
