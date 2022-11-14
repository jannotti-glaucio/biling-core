package tech.jannotti.billing.core.banking.santander.persistence.repository;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderBilletRegistryResponse;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.santander.billetRegistryResponseRepository")
public interface BilletRegistryResponseRepository extends AbstractRepository<SantanderBilletRegistryResponse, Long> {

}
