package tech.jannotti.billing.core.services.bank;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.persistence.enums.BankRemittanceStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBank;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankRemittance;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealerAddress;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealerBankAccount;
import tech.jannotti.billing.core.persistence.repository.base.bank.BankRemittanceRepository;
import tech.jannotti.billing.core.persistence.repository.base.bank.BankRepository;
import tech.jannotti.billing.core.persistence.repository.base.company.CompanyBankAccountRepository;
import tech.jannotti.billing.core.persistence.repository.base.dealer.DealerAddressRepository;
import tech.jannotti.billing.core.persistence.repository.base.dealer.DealerBankAccountRepository;
import tech.jannotti.billing.core.services.AbstractService;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;

@Service
public class BankingService extends AbstractService {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private CompanyBankAccountRepository companyBankAccountRepository;

    @Autowired
    private DealerBankAccountRepository dealerBankAccountRepository;

    @Autowired
    private BankRemittanceRepository bankRemittanceRepository;

    @Autowired
    private DealerAddressRepository dealerAddressRepository;

    public BaseBank getBank(String code) {
        return bankRepository.getByCode(code);
    }
    
    public List<BaseBank> getBanks() {
        return bankRepository.findAll();
    }

    public BaseCompanyBankAccount getCompanyBankAccount(long id) {
        return companyBankAccountRepository.findOne(id);
    }

    public BaseCompanyBankAccount getCompanyBankAccount(BaseCompany company, String token) {
        return companyBankAccountRepository.getByCompanyAndToken(company, token);
    }

    public BaseDealerBankAccount getDealerBankAccount(BaseDealer dealer, String token) {
        return dealerBankAccountRepository.getByDealerAndToken(dealer, token);
    }

    public BaseDealerAddress getBeneficiaryAddress(BaseDealer dealer) {

        BaseDealerAddress address = dealerAddressRepository.getByDealerAndBillingAddress(dealer, true);
        if (address == null)
            throw new ResultCodeServiceException(CODE_DEALER_BILLING_ADDRESS_REQUIRED);
        else
            return address;
    }

    public void updateRemittanceStatus(BaseBankRemittance bankRemittance, BankRemittanceStatusEnum status) {
        LocalDateTime processingDate = DateTimeHelper.getNowDateTime();
        bankRemittanceRepository.updateStatusAndProcessingDateById(bankRemittance.getId(), status, processingDate);
    }

}
