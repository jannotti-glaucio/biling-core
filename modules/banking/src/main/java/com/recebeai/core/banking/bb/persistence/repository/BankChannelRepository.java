package tech.jannotti.billing.core.banking.bb.persistence.repository;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.banking.bb.persistence.model.BancoBrasilBankChannel;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankChannel;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository("banking.bb.bankChannelRepository")
public interface BankChannelRepository extends AbstractRepository<BancoBrasilBankChannel, Integer> {

    BancoBrasilBankChannel getByBaseBankChannel(BaseBankChannel baseBankChannel);

}
