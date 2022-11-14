package tech.jannotti.billing.core.services.dealer;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.enums.CRUDOperationEnum;
import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.persistence.enums.EntityChildStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealerBankAccount;
import tech.jannotti.billing.core.persistence.repository.base.dealer.DealerBankAccountRepository;
import tech.jannotti.billing.core.services.bank.AbstractEntityBankAccountService;
import tech.jannotti.billing.core.services.dto.request.bank.BankAccountServiceRequest;
import tech.jannotti.billing.core.services.dto.request.bank.UpdateBankAccountServiceRequest;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;

@Service
public class DealerBankAccountService extends AbstractEntityBankAccountService {

    @Autowired
    private DealerBankAccountRepository bankAccountRepository;

    List<BaseDealerBankAccount> findActiveAccounts(BaseDealer dealer) {
        return bankAccountRepository.findByDealerAndStatus(dealer, EntityChildStatusEnum.ACTIVE);
    }

    @Transactional
    void addAccounts(BaseDealer dealer, List<BankAccountServiceRequest> requestAccounts) {

        if (CollectionUtils.isNotEmpty(requestAccounts)) {
            for (BankAccountServiceRequest account : requestAccounts)
                addAccount(dealer, account);
        }
    }

    private void addAccount(BaseDealer dealer, BankAccountServiceRequest request) {

        BaseDealerBankAccount account = dtoMapper.map(request, BaseDealerBankAccount.class);
        account.setDealer(dealer);
        account.setToken(generateToken());
        account.setStatus(EntityChildStatusEnum.ACTIVE);
        bankAccountRepository.save(account);
    }

    @Transactional
    void updateAccounts(BaseDealer dealer, List<UpdateBankAccountServiceRequest> requests) {

        if (CollectionUtils.isEmpty(requests))
            return;

        // Separa as requisicoes de contas por tipo de operacao
        List<UpdateBankAccountServiceRequest> requestsToUpdate = filterUpdateBankAccountRequests(requests,
            CRUDOperationEnum.UPDATE);
        List<UpdateBankAccountServiceRequest> requestsToAdd = filterUpdateBankAccountRequests(requests, CRUDOperationEnum.ADD);
        List<UpdateBankAccountServiceRequest> requestsToDelete = filterUpdateBankAccountRequests(requests,
            CRUDOperationEnum.DELETE);

        // Primeiro exlclui as contas
        for (UpdateBankAccountServiceRequest request : requestsToDelete) {
            deleteAccount(dealer, request);
        }

        // Depois atualia as contas existentes
        for (UpdateBankAccountServiceRequest request : requestsToUpdate) {
            updateAccount(dealer, request);
        }

        // Por fim inclui as novas contas
        for (UpdateBankAccountServiceRequest request : requestsToAdd) {
            addAccount(dealer, request);
        }
    }

    @Transactional
    void deleteBankAccountes(List<BaseDealerBankAccount> bankAccountes) {

        if (CollectionUtils.isNotEmpty(bankAccountes)) {
            for (BaseDealerBankAccount bankAccount : bankAccountes) {
                if (!bankAccount.getStatus().equals(EntityChildStatusEnum.DELETED))
                    deleteBankAccount(bankAccount);
            }
        }
    }

    private void updateAccount(BaseDealer dealer, UpdateBankAccountServiceRequest request) {

        BaseDealerBankAccount bankAccount = bankAccountRepository.getByDealerAndToken(dealer, request.getToken());
        if (bankAccount == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_ACCOUNT_TOKEN_NOT_FOUND, request.getToken());

        dtoMapper.map(request, bankAccount);
        bankAccountRepository.save(bankAccount);
    }

    private void deleteAccount(BaseDealer dealer, UpdateBankAccountServiceRequest request) {

        BaseDealerBankAccount bankAccount = bankAccountRepository.getByDealerAndTokenAndStatus(dealer, request.getToken(),
            EntityChildStatusEnum.ACTIVE);
        if (bankAccount == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_ACCOUNT_TOKEN_NOT_FOUND, request.getToken());

        deleteBankAccount(bankAccount);
    }

    private void deleteBankAccount(BaseDealerBankAccount bankAccount) {
        bankAccountRepository.updateStatusById(bankAccount.getId(), EntityChildStatusEnum.DELETED);
    }

}
