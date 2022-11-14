package tech.jannotti.billing.core.services.market;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.persistence.enums.MarketStatementDirectionEnum;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealerMarketAccount;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketAccount;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketBalance;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketStatement;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketStatementType;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketWithdraw;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePayment;
import tech.jannotti.billing.core.persistence.repository.base.market.MarketStatementRepository;
import tech.jannotti.billing.core.persistence.repository.base.market.MarketStatementTypeRepository;

@Service
public class MarketStatementService extends AbstractMarketService {

    @Autowired
    private MarketStatementTypeRepository marketStatementTypeRepository;

    @Autowired
    private MarketStatementRepository marketStatementRepository;

    @Autowired
    private MarketAccountService marketAccountService;

    public List<BaseMarketStatement> findStatements(BaseDealerMarketAccount marketAccount, LocalDate startDate,
        LocalDate endDate) {

        List<BaseMarketStatement> statements = marketStatementRepository
            .findByMarketAccountAndStatementDateBetweenOrderByStatementDateAscIdAsc(marketAccount, startDate, endDate);

        int priorBalance = 0;

        // Verifica se tem saldo no dia anterior ao inicio da consulta
        BaseMarketBalance startBalance = marketAccountService.getFirstBalanceByDate(marketAccount, startDate);
        if (startBalance != null) {
            priorBalance = startBalance.getCurrentBalance();

        } else {
            // Se nao tem saldo, calcula o saldo ateh o dia anterior ao inicio da consulta

            // Soma os creditos antes da data inicial da consulta
            Integer creditsSummary = marketStatementRepository.sumAmountByMarketAccountAndDirectionAndStatementDateLessThan(
                marketAccount, MarketStatementDirectionEnum.CREDIT, startDate);
            if (creditsSummary == null)
                creditsSummary = 0;

            // Soma os debitos antes da data inicial da consulta
            Integer debitsSummary = marketStatementRepository.sumAmountByMarketAccountAndDirectionAndStatementDateLessThan(
                marketAccount, MarketStatementDirectionEnum.DEBIT, startDate);
            if (debitsSummary == null)
                debitsSummary = 0;

            priorBalance = creditsSummary - debitsSummary;
        }

        // Calcula a coluna saldo atual de cada operacao
        int balance = priorBalance;
        for (BaseMarketStatement statement : statements) {

            if (statement.getType().getDirection().equals(MarketStatementDirectionEnum.CREDIT))
                balance = balance + statement.getAmount();
            else
                balance = balance - statement.getAmount();
            statement.setBalance(balance);
        }

        return statements;
    }

    private BaseMarketStatement createStatement(BaseMarketAccount marketAccount, String statementTypeCode, LocalDate paymentDate,
        int amount) {

        BaseMarketStatementType statementType = marketStatementTypeRepository.getByCode(statementTypeCode);

        BaseMarketStatement statement = new BaseMarketStatement();
        statement.setMarketAccount(marketAccount);
        statement.setToken(generateStatementToken());
        statement.setType(statementType);
        statement.setStatementDate(paymentDate);
        statement.setAmount(amount);
        return statement;
    }

    @Transactional
    public BaseMarketStatement addStatement(BaseMarketAccount marketAccount, String statementTypeCode, LocalDate paymentDate,
        int amount, BaseMarketWithdraw withdraw) {

        BaseMarketStatement statement = createStatement(marketAccount, statementTypeCode, paymentDate, amount);
        statement.setMarketWithdraw(withdraw);
        marketStatementRepository.save(statement);
        return statement;
    }

    @Transactional
    public BaseMarketStatement addStatement(BaseMarketAccount marketAccount, String statementTypeCode, LocalDate paymentDate,
        int amount, BasePayment payment) {

        BaseMarketStatement statement = createStatement(marketAccount, statementTypeCode, paymentDate, amount);
        statement.setPayment(payment);
        marketStatementRepository.save(statement);
        return statement;
    }

    private String generateStatementToken() {
        return tokenGenerator.generateRandomHexToken("marketStatement.token", 20);
    }

}
