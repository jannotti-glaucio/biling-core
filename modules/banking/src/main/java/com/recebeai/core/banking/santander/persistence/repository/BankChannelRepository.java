package tech.jannotti.billing.core.banking.santander.persistence.repository;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.santander.persistence.model.SantanderBankChannel;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankChannel;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.santander.bankChannelRepository")
public interface BankChannelRepository extends AbstractRepository<SantanderBankChannel, Integer> {

    SantanderBankChannel getByBaseBankChannel(BaseBankChannel baseBankChannel);

}
