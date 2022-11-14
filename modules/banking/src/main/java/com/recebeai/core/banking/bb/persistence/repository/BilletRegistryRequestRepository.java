package tech.jannotti.billing.core.banking.bb.persistence.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilBilletRegistryRequest;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.bb.billetRegistryRequestRepository")
public interface BilletRegistryRequestRepository extends AbstractRepository<BancoBrasilBilletRegistryRequest, Long> {

    public List<BancoBrasilBilletRegistryRequest> findByBankBilletCompanyBankAccountAndTextoNumeroTituloBeneficiario(
        BaseCompanyBankAccount companyBankAccount, long textoNumeroTituloBeneficiario);

    public BancoBrasilBilletRegistryRequest getByBankBillet(BasePaymentBankBillet bankBillet);

}
