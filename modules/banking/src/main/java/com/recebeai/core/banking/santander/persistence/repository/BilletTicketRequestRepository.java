package tech.jannotti.billing.core.banking.santander.persistence.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderBilletTicketRequest;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.santander.billetTicketRequestRepository")
public interface BilletTicketRequestRepository extends AbstractRepository<SantanderBilletTicketRequest, Long> {

    public List<SantanderBilletTicketRequest> findByBankBilletCompanyBankAccountAndTituloSeuNumero(
        BaseCompanyBankAccount companyBankAccount, long tituloSeuNumero);

    public SantanderBilletTicketRequest getByBankBillet(BasePaymentBankBillet bankBillet);

}
