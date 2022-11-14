package tech.jannotti.billing.core.banking.bb.persistence.repository;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilBilletRegistryResponse;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.bb.billetRegistryResponseRepository")
public interface BilletRegistryResponseRepository extends AbstractRepository<BancoBrasilBilletRegistryResponse, Long> {

}
