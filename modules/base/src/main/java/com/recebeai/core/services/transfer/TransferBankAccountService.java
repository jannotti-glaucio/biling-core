package tech.jannotti.billing.core.services.transfer;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.constants.enums.TransferTypeConstants;
import tech.jannotti.billing.core.persistence.enums.TransferStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketWithdraw;
import tech.jannotti.billing.core.persistence.model.base.transfer.BaseTransferBankAccount;
import tech.jannotti.billing.core.persistence.model.base.transfer.BaseTransferType;
import tech.jannotti.billing.core.persistence.repository.base.transfer.TransferBankAccountRepository;

@Service
public class TransferBankAccountService extends AbstractTransferService {

    @Autowired
    private TransferBankAccountRepository transferBankAccountRepository;

    @Transactional
    private void add(BaseTransferBankAccount transfer, String transferTypeCode, int amount, int transferCost,
        LocalDate transferDate) {

        BaseTransferType transferType = transferTypeRepository.getByCode(transferTypeCode);

        transfer.setToken(generateToken());
        transfer.setTransferType(transferType);
        transfer.setAmount(amount);
        transfer.setTransferCost(transferCost);
        transfer.setTransferDate(transferDate);
        transfer.setStatus(TransferStatusEnum.PENDING);
        transfer.setCreationDate(DateTimeHelper.getNowDateTime());

        transferBankAccountRepository.save(transfer);
    }

    public BaseTransferBankAccount add(BaseMarketWithdraw marketWithdraw, LocalDate transferDate,
        BaseCompanyBankAccount companyBankAccount) {

        BaseTransferBankAccount transfer = new BaseTransferBankAccount();
        transfer.setSourceBankAccount(companyBankAccount);
        transfer.setDestinationBankAccount(marketWithdraw.getBankAccount());
        transfer.setMarketWithdraw(marketWithdraw);

        // Soh aplica o custo da transferencia se forem contas de banco diferentes
        Integer transferCost = null;
        if (companyBankAccount.getBank().getId().equals(marketWithdraw.getBankAccount().getBank().getId()))
            transferCost = 0;
        else
            transferCost = companyBankAccount.getInterbankTransferFee();

        add(transfer, TransferTypeConstants.COMPANY_TO_DEALER_BANK_ACCOUNT.getCode(), marketWithdraw.getNetAmount(), transferCost,
            transferDate);

        return transfer;
    }

    public void done(BaseMarketWithdraw marketWithdraw) {

        BaseTransferBankAccount transfer = transferBankAccountRepository.getByMarketWithdrawAndStatus(marketWithdraw,
            TransferStatusEnum.PENDING);

        LocalDateTime processingDate = DateTimeHelper.getNowDateTime();
        transferBankAccountRepository.updateStatusAndProcessingDateById(transfer.getId(), TransferStatusEnum.DONE,
            processingDate);
    }

}
