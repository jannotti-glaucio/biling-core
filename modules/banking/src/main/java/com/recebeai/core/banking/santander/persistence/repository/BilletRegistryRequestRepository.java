package tech.jannotti.billing.core.banking.santander.persistence.repository;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderBilletRegistryRequest;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.santander.billetRegistryRequestRepository")
public interface BilletRegistryRequestRepository extends AbstractRepository<SantanderBilletRegistryRequest, Long> {

}
