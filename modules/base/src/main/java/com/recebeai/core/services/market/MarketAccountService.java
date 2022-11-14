package tech.jannotti.billing.core.services.market;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.enums.MarketStatementDirectionEnum;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketAccount;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketBalance;
import tech.jannotti.billing.core.persistence.repository.base.market.MarketAccountRepository;
import tech.jannotti.billing.core.persistence.repository.base.market.MarketBalanceRepository;
import tech.jannotti.billing.core.persistence.repository.base.market.MarketStatementRepository;

@Service
public class MarketAccountService extends AbstractMarketService {

    private LogManager logManager = LogFactory.getManager(MarketAccountService.class);

    @Autowired
    private MarketAccountRepository marketAccountRepository;

    @Autowired
    private MarketBalanceRepository marketBalanceRepository;

    @Autowired
    private MarketStatementRepository marketStatementRepository;

    public long getCurrentBalance(BaseMarketAccount marketAccount) {

        // Consulta o ultimo saldo
        BaseMarketBalance lastBalance = marketBalanceRepository.getFirstByMarketAccountOrderByBalanceDateDesc(marketAccount);

        LocalDate lastBalanceDate = null;
        if (lastBalance != null) {
            lastBalanceDate = lastBalance.getBalanceDate();

        } else {
            // Se nao tem saldo algum, busca a data da primeira operacao
            LocalDate lastStatementDate = marketStatementRepository.minimumStatementDateByMarketAccount(marketAccount);

            if (lastStatementDate != null)
                lastBalanceDate = lastStatementDate.minusDays(1);
            else
                // Se nao tem saldo algum, busca a data da primeira operacao
                return 0;
        }

        // Soma os creditos da data do ultimo saldo ateh hoje
        Integer creditsSummary = marketStatementRepository.sumAmountByMarketAccountAndDirectionAndStatementDateGreaterThan(
            marketAccount, MarketStatementDirectionEnum.CREDIT, lastBalanceDate);
        if (creditsSummary == null)
            creditsSummary = 0;

        // Soma os debitos da data do ultimo saldo ateh hoje
        Integer debitsSummary = marketStatementRepository.sumAmountByMarketAccountAndDirectionAndStatementDateGreaterThan(
            marketAccount, MarketStatementDirectionEnum.DEBIT, lastBalanceDate);
        if (debitsSummary == null)
            debitsSummary = 0;

        if (lastBalance != null)
            return lastBalance.getCurrentBalance() + creditsSummary - debitsSummary;
        else
            return creditsSummary - debitsSummary;
    }

    public BaseMarketBalance getFirstBalanceByDate(BaseMarketAccount marketAccount, LocalDate startDate) {
        return marketBalanceRepository.getFirstByMarketAccountAndBalanceDateLessThanOrderByBalanceDateDesc(marketAccount,
            startDate);
    }

    public void processBalancesSummary() {

        List<BaseMarketAccount> marketAccounts = marketAccountRepository.findByStatus(EntityStatusEnum.ACTIVE);
        for (BaseMarketAccount marketAccount : marketAccounts) {
            logManager.logDEBUG("Sumarizando saldo da Conta Virtual [%s]", marketAccount.getToken());

            // Pega o ultimo saldo
            BaseMarketBalance lastBalance = marketBalanceRepository.getFirstByMarketAccountOrderByBalanceDateDesc(marketAccount);

            LocalDate nextBalanceDate = null;
            if (lastBalance != null)
                // Se tem saldo, assume como data do proximo saldo a data anterior a ele
                nextBalanceDate = lastBalance.getBalanceDate().plusDays(1);

            else {
                // Se nao tem saldo algum, busca a data da primeira operacao
                LocalDate lastStatementDate = marketStatementRepository.minimumStatementDateByMarketAccount(marketAccount);

                if (lastStatementDate != null) {
                    // Se achou uma primeira operacao, assume como data do proximo saldo a data dela
                    nextBalanceDate = lastStatementDate;
                } else {
                    // Se achou nao teve saldo anterior e nao teve nenhuma operacao, nao precisa processar
                    logManager.logDEBUG("Nao existem operacoes para sumarizar nessa Conta Virtual");
                    continue;
                }
            }

            // TODO Quando criar o job pra aplicar o credito na conta apenas apos o deposito do credito nao precisa mais de 2 dias
            LocalDate endBalanceData = DateTimeHelper.getNowDateMinusDays(2);
            if (nextBalanceDate.isEqual(endBalanceData) || nextBalanceDate.isAfter(endBalanceData)) {
                logManager.logDEBUG("Nao existem datas para sumarizar nessa Conta Virtual");
                continue;
            }
            logManager.logDEBUG("Sumarizando saldo de [%s] ateh [%s]", nextBalanceDate, endBalanceData);

            int priorBalance = 0;
            if (lastBalance != null)
                priorBalance = lastBalance.getCurrentBalance();

            LocalDate balanceDate = nextBalanceDate;
            while (!balanceDate.isAfter(endBalanceData)) {
                logManager.logDEBUG("Processando saldo do dia [%s]", balanceDate);

                // Soma os creditos da data
                Integer creditsSummary = marketStatementRepository.sumAmountByMarketAccountAndDirectionAndStatementDate(
                    marketAccount, MarketStatementDirectionEnum.CREDIT, balanceDate);
                if (creditsSummary == null)
                    creditsSummary = 0;

                // Soma os debitos da data
                Integer debitsSummary = marketStatementRepository.sumAmountByMarketAccountAndDirectionAndStatementDate(
                    marketAccount, MarketStatementDirectionEnum.DEBIT, balanceDate);
                if (debitsSummary == null)
                    debitsSummary = 0;

                BaseMarketBalance marketBalance = new BaseMarketBalance();
                marketBalance.setMarketAccount(marketAccount);
                marketBalance.setBalanceDate(balanceDate);
                marketBalance.setDebitsSummary(debitsSummary);
                marketBalance.setCreditsSummary(creditsSummary);
                marketBalance.setPriorBalance(priorBalance);

                logManager.logINFO("Criando saldo do dia [%s]", balanceDate);
                marketBalanceRepository.save(marketBalance);

                priorBalance = marketBalance.getCurrentBalance();
                balanceDate = balanceDate.plusDays(1);
            }
        }
    }

}
