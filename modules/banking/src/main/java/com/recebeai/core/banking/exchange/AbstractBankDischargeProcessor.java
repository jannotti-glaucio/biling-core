package tech.jannotti.billing.core.banking.exchange;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBank;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;
import tech.jannotti.billing.core.persistence.repository.base.bank.BankRepository;
import tech.jannotti.billing.core.persistence.repository.base.company.CompanyBankAccountRepository;
import tech.jannotti.billing.core.persistence.repository.base.payment.PaymentBankBilletRepository;
import tech.jannotti.billing.core.services.bank.BankingService;

public abstract class AbstractBankDischargeProcessor {

    private static final LogManager logManager = LogFactory.getManager(AbstractBankDischargeProcessor.class);

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private CompanyBankAccountRepository companyBankAccountRepository;

    @Autowired
    private PaymentBankBilletRepository bankBilletRepository;

    @Autowired
    protected BankingService bankingService;

    protected abstract String getBankNumber();

    public void execute() {

        BaseBank baseBank = bankRepository.getByCode(getBankNumber());
        if (baseBank == null) {
            logManager.logERROR("Banco [%s] nao localizado", getBankNumber());
            return;
        }

        List<BaseCompanyBankAccount> companyBankAccounts = companyBankAccountRepository.findByBank(baseBank);

        if (companyBankAccounts != null) {
            for (BaseCompanyBankAccount companyBankAccount : companyBankAccounts) {
                logManager.logINFO("Processando retornos da conta [token=%s]", companyBankAccount.getToken());
                processDischarges(companyBankAccount);
            }
        }
    }

    protected BasePaymentBankBillet getBankBilletByOurNumber(BaseCompanyBankAccount companyBankAccount, long ourNumber) {
        return bankBilletRepository.getByCompanyBankAccountAndOurNumber(companyBankAccount, ourNumber);
    }

    protected BasePaymentBankBillet getBankBilletByYourNumber(BaseCompanyBankAccount companyBankAccount, long yourNumber) {
        return bankBilletRepository.getByCompanyBankAccountAndYourNumber(companyBankAccount, yourNumber);
    }

    protected abstract void processDischarges(BaseCompanyBankAccount baseCompanyBankAccount);

}
