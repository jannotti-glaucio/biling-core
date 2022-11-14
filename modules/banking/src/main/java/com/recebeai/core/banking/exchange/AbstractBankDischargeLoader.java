package tech.jannotti.billing.core.banking.exchange;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.connector.banking.exception.BankingExchangeException;
import tech.jannotti.billing.core.services.bank.BankingService;

public abstract class AbstractBankDischargeLoader {

    private static final LogManager logManager = LogFactory.getManager(AbstractBankDischargeLoader.class);

    @Autowired
    protected BankingService bankingService;

    protected void moveRejectedDischargeFile(File file, File rejectedDir) {
        try {
            logManager.logINFO("Movendo arquivo rejeitado [" + file.getName() + "] para [" + rejectedDir.getPath() + "]");
            FileUtils.moveFileToDirectory(file, rejectedDir, true);
        } catch (IOException e) {
            throw new BankingExchangeException("Erro movendo arquivo rejeitado", e);
        }
    }

    protected void moveProcessedDischargeFile(File file, File processedDir) {
        try {
            logManager.logINFO("Movendo arquivo processado [" + file.getName() + "] para [" + processedDir.getPath() + "]");
            FileUtils.moveFileToDirectory(file, processedDir, true);
        } catch (IOException e) {
            throw new BankingExchangeException("Erro movendo arquivo processado", e);
        }
    }

}
