package tech.jannotti.billing.core.banking.exchange;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.persistence.enums.PaymentStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.bank.BaseBank;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePaymentBankBillet;
import tech.jannotti.billing.core.persistence.repository.base.bank.BankRepository;
import tech.jannotti.billing.core.persistence.repository.base.company.CompanyBankAccountRepository;
import tech.jannotti.billing.core.persistence.repository.base.payment.PaymentBankBilletRepository;
import tech.jannotti.billing.core.services.payment.BankBilletService;

public abstract class AbstractUnpaidBankBilletsProcessor {

    private static final LogManager logManager = LogFactory.getManager(AbstractUnpaidBankBilletsProcessor.class);

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private CompanyBankAccountRepository companyBankAccountRepository;

    @Autowired
    private PaymentBankBilletRepository bankBilletRepository;

    @Autowired
    private BankBilletService bankBilletService;

    protected abstract String getBankNumber();

    public void execute() {

        BaseBank baseBank = bankRepository.getByCode(getBankNumber());
        if (baseBank == null) {
            logManager.logERROR("Banco [%s] nao localizado", getBankNumber());
            return;
        }

        List<BaseCompanyBankAccount> companyBankAccounts = companyBankAccountRepository.findByBank(baseBank);
        if (companyBankAccounts.isEmpty()) {
            logManager.logWARN("Nao existem contas para o banco [%s]", getBankNumber());
            return;
        }

        for (BaseCompanyBankAccount companyBankAccount : companyBankAccounts) {
            LocalDate expirationDate = DateTimeHelper.getNowDateMinusDays(companyBankAccount.getBilletUnpaidLimit());

            logManager.logINFO("Processando boletos da conta [token=%s] nao pagos a %s dias", companyBankAccount.getToken(),
                companyBankAccount.getBilletUnpaidLimit());

            List<BasePaymentBankBillet> bankBillets = bankBilletRepository
                .findByCompanyBankAccountAndExpirationDateLessThanAndStatus(companyBankAccount, expirationDate,
                    PaymentStatusEnum.AUTHORIZED);

            if (bankBillets.isEmpty()) {
                logManager.logINFO("Nao existem boletos nao pagos para processar na conta [token=%s]",
                    companyBankAccount.getToken());
                continue;
            }

            for (BasePaymentBankBillet bankBillet : bankBillets) {
                logManager.logINFO("Cancelando boleto nao pago [token=%s]", bankBillet.getToken());
                bankBilletService.cancel(bankBillet);
            }
        }
    }

}
