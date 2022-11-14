package tech.jannotti.billing.core.persistence.repository.base.transfer;

import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.TransferStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketWithdraw;
import tech.jannotti.billing.core.persistence.model.base.transfer.BaseTransferBankAccount;

@Repository
public interface TransferBankAccountRepository extends AbstractTransferRepository<BaseTransferBankAccount> {

    BaseTransferBankAccount getByMarketWithdrawAndStatus(BaseMarketWithdraw marketWithdraw, TransferStatusEnum status);

}