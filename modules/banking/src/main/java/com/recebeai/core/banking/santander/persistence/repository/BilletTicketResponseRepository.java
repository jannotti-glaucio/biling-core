package tech.jannotti.billing.core.banking.santander.persistence.repository;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderBilletTicketResponse;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.santander.billetTicketResponseRepository")
public interface BilletTicketResponseRepository extends AbstractRepository<SantanderBilletTicketResponse, Long> {

}
