package tech.jannotti.billing.core.banking.exchange;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.commons.bean.NormalizerHelper;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.persistence.enums.BankRemittanceStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBank;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBankRemittance;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.repository.base.bank.BankRemittanceRepository;
import tech.jannotti.billing.core.persistence.repository.base.bank.BankRepository;
import tech.jannotti.billing.core.persistence.repository.base.company.CompanyBankAccountRepository;
import tech.jannotti.billing.core.services.bank.BankingService;

public abstract class AbstractBankRemittanceProcessor {

    private static final LogManager logManager = LogFactory.getManager(AbstractBankRemittanceProcessor.class);

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private CompanyBankAccountRepository companyBankAccountRepository;

    @Autowired
    private BankRemittanceRepository bankRemittanceRepository;

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
        if (companyBankAccounts.isEmpty()) {
            logManager.logINFO("Nao existem contas para o banco [%s]", getBankNumber());
            return;
        }

        for (BaseCompanyBankAccount companyBankAccount : companyBankAccounts) {
            logManager.logINFO("Processando remessas da conta [token=%s]", companyBankAccount.getToken());

            List<BaseBankRemittance> baseRemittances = bankRemittanceRepository.findByBankAccountAndStatus(companyBankAccount,
                BankRemittanceStatusEnum.PENDING);

            if (baseRemittances.isEmpty()) {
                logManager.logINFO("Nao existem remessas para processar para a conta [token=%s]", companyBankAccount.getToken());
                continue;
            }

            processRemittances(companyBankAccount, baseRemittances);
        }
    }

    protected String normalize(String value, int size) {
        return NormalizerHelper.normalize(value, size).toUpperCase();
    }

    protected void discardRemittanceFile(String filePath) {
        File file = new File(filePath);

        try {
            FileUtils.forceDelete(file);
        } catch (IOException e) {
            logManager.logERROR("Erro descartando arquivo de remessa [" + file.getPath() + "]", e);
        }
    }

    protected abstract void processRemittances(BaseCompanyBankAccount baseCompanyBankAccount,
        List<BaseBankRemittance> baseRemittances);

}
