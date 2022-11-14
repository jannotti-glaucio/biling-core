package tech.jannotti.billing.core.services.dealer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.constants.enums.AlertTypeConstants;
import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.enums.MarketWithdrawStatusEnum;
import tech.jannotti.billing.core.persistence.enums.MediaTypeEnum;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealerMarketAccount;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketStatement;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketWithdraw;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePayment;
import tech.jannotti.billing.core.persistence.model.base.user.BaseUser;
import tech.jannotti.billing.core.persistence.repository.base.dealer.DealerMarketAccountRepository;
import tech.jannotti.billing.core.services.AlertingService;
import tech.jannotti.billing.core.services.dto.request.MarketWithdrawServiceRequest;
import tech.jannotti.billing.core.services.dto.response.market.GetMarketWithdrawFeesServiceResponse;
import tech.jannotti.billing.core.services.dto.response.market.SummarizeMarketWithdrawsServiceResponse;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;
import tech.jannotti.billing.core.services.market.AbstractEntityMarketService;

@Service
public class DealerMarketService extends AbstractEntityMarketService {

    @Autowired
    private DealerMarketAccountRepository marketAccountRepository;

    @Autowired
    private AlertingService alertingService;

    // Market Accounts

    public void add(BaseDealer dealer) {

        BaseDealerMarketAccount marketAccount = new BaseDealerMarketAccount();
        marketAccount.setDealer(dealer);
        marketAccount.setToken(generateToken());
        marketAccount.setStatus(EntityStatusEnum.ACTIVE);

        marketAccountRepository.save(marketAccount);
    }

    private BaseDealerMarketAccount getActiveAccount(BaseDealer dealer) {

        BaseDealerMarketAccount marketAccount = marketAccountRepository.getByDealerAndStatus(dealer, EntityStatusEnum.ACTIVE);
        if (marketAccount == null)
            throw new ResultCodeServiceException(CODE_ACTIVE_MARKET_ACCOUNT_REQUIRED);
        else
            return marketAccount;
    }

    private List<BaseDealerMarketAccount> getActiveAccounts(BaseCompany company) {
        return marketAccountRepository.findByDealerCompanyAndStatus(company, EntityStatusEnum.ACTIVE);
    }

    public long getCurrentBalance(BaseDealer dealer) {

        BaseDealerMarketAccount marketAccount = getActiveAccount(dealer);
        return marketAccountService.getCurrentBalance(marketAccount);
    }

    // Statements

    public List<BaseMarketStatement> findStatements(BaseDealer dealer, LocalDate startDate, LocalDate endDate) {

        BaseDealerMarketAccount marketAccount = getActiveAccount(dealer);
        return marketStatementService.findStatements(marketAccount, startDate, endDate);
    }

    public BaseMarketStatement addStatement(BaseDealer dealer, String statementTypeCode, LocalDate paymentDate, int amount,
        BasePayment payment) {

        BaseDealerMarketAccount marketAccount = getActiveAccount(dealer);
        return marketStatementService.addStatement(marketAccount, statementTypeCode, paymentDate, amount, payment);
    }

    // Withdraws

    public Page<BaseMarketWithdraw> findWithdraws(BaseDealer dealer, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        BaseDealerMarketAccount marketAccount = getActiveAccount(dealer);

        return marketWithdrawService.findWithdraws(marketAccount, startDate, endDate, pageable);
    }

    public BaseMarketWithdraw getWithdraw(String token) {
        return marketWithdrawService.getWithdraw(token);
    }

    public Page<BaseMarketWithdraw> findWithdraws(BaseCompany company, BaseDealer dealer, LocalDate startDate,
        LocalDate endDate, MarketWithdrawStatusEnum[] status, Pageable pageable) {

        List<BaseDealerMarketAccount> marketAccounts = buildMarketAccountsList(company, dealer);
        return marketWithdrawService.findWithdraws(marketAccounts, startDate, endDate, status, pageable);
    }

    public List<BaseMarketWithdraw> findWithdraws(BaseCompany company, BaseDealer dealer, LocalDate startDate,
        LocalDate endDate, MarketWithdrawStatusEnum[] status) {

        List<BaseDealerMarketAccount> marketAccounts = buildMarketAccountsList(company, dealer);
        return marketWithdrawService.findWithdraws(marketAccounts, startDate, endDate, status);
    }

    public SummarizeMarketWithdrawsServiceResponse summarizeWithdraws(BaseCompany company, BaseDealer dealer, LocalDate startDate,
        LocalDate endDate, MarketWithdrawStatusEnum[] status) {

        List<BaseDealerMarketAccount> marketAccounts = buildMarketAccountsList(company, dealer);
        return marketWithdrawService.summarizeWithdraws(marketAccounts, startDate, endDate, status);
    }

    public Page<BaseMarketWithdraw> findPendingWithdraws(BaseCompany company, BaseDealer dealer, Pageable pageable) {

        List<BaseDealerMarketAccount> marketAccounts = buildMarketAccountsList(company, dealer);
        return marketWithdrawService.findPendingWithdraws(marketAccounts, pageable);
    }

    public SummarizeMarketWithdrawsServiceResponse summarizePendingWithdraws(BaseCompany company, BaseDealer dealer) {

        List<BaseDealerMarketAccount> marketAccounts = buildMarketAccountsList(company, dealer);
        return marketWithdrawService.summarizePendingWithdraws(marketAccounts);
    }

    private List<BaseDealerMarketAccount> buildMarketAccountsList(BaseCompany company, BaseDealer dealer) {
        List<BaseDealerMarketAccount> marketAccounts = null;

        if (dealer != null) {
            BaseDealerMarketAccount marketAccount = getActiveAccount(dealer);
            marketAccounts = new ArrayList<BaseDealerMarketAccount>();
            marketAccounts.add(marketAccount);
        } else
            marketAccounts = getActiveAccounts(company);
        return marketAccounts;
    }

    @Transactional
    public BaseMarketWithdraw addWithdraw(BaseDealer dealer, BaseUser user, MarketWithdrawServiceRequest request) {

        BaseDealerMarketAccount marketAccount = getActiveAccount(dealer);
        validateWithdrawAmount(dealer, request.getAmount());

        int withdrawFee = marketAccount.getDealer().getBillingPlan().getMarketWithdrawFee();
        BaseMarketWithdraw withdraw = marketWithdrawService.addWithdraw(marketAccount, user, request, withdrawFee);

        // Envia o alerta de solicitacao de saque
        alertingService.dispatchWithdrawAlert(withdraw, MediaTypeEnum.EMAIL, AlertTypeConstants.MARKET_WITHDRAW_REQUEST);

        return withdraw;
    }

    @Transactional
    public GetMarketWithdrawFeesServiceResponse getWithdrawFees(BaseDealer dealer, MarketWithdrawServiceRequest request) {

        validateWithdrawAmount(dealer, request.getAmount());

        int fees = dealer.getBillingPlan().getMarketWithdrawFee();
        int netAmount = request.getAmount() - fees;
        return new GetMarketWithdrawFeesServiceResponse(fees, netAmount);
    }

    private void validateWithdrawAmount(BaseDealer dealer, int amount) {

        int minimumAmount = dealer.getCompany().getMinimumMarketWithdrawAmount();
        if (amount < minimumAmount)
            throw new ResultCodeServiceException(CODE_WITHDRAW_AMOUNT_BELOW_MINIMUM);

        int maximumAmount = dealer.getCompany().getMaximumMarketWithdrawAmount();
        if (amount > maximumAmount)
            throw new ResultCodeServiceException(CODE_WITHDRAW_AMOUNT_ABOVE_MAXIMUM);

        int withdrawFee = dealer.getBillingPlan().getMarketWithdrawFee();
        if ((amount - withdrawFee) <= 0)
            throw new ResultCodeServiceException(CODE_WITHDRAW_NET_AMOUNT_BELOW_MINIMUM);
    }

    private BaseMarketWithdraw getWithdrawAccount(BaseCompany company, String token) {

        BaseMarketWithdraw withdraw = marketWithdrawService.getWithdraw(token);
        if (withdraw == null)
            throw new ResultCodeServiceException(CODE_TOKEN_NOT_FOUND, token);

        // Valida se o token eh de uma conta de Dealer
        BaseDealerMarketAccount marketAccount = null;
        if (withdraw.getMarketAccount() instanceof BaseDealerMarketAccount)
            marketAccount = (BaseDealerMarketAccount) withdraw.getMarketAccount();
        else
            throw new ResultCodeServiceException(CODE_TOKEN_NOT_FOUND, token);

        // Valida se o a conta eh de um Dealer da Company
        if (!marketAccount.getDealer().getCompany().getToken().equals(company.getToken()))
            throw new ResultCodeServiceException(CODE_TOKEN_NOT_FOUND, token);

        // Valida se a conta estah ativa
        if (!marketAccount.getStatus().equals(EntityStatusEnum.ACTIVE))
            throw new ResultCodeServiceException(CODE_ACTIVE_MARKET_ACCOUNT_REQUIRED);

        return withdraw;
    }

    public void approveWithdraw(BaseCompany company, String token, BaseUser reviewerUser) {

        BaseMarketWithdraw withdraw = getWithdrawAccount(company, token);
        BaseDealerMarketAccount marketAccount = (BaseDealerMarketAccount) withdraw.getMarketAccount();
        BaseCompanyBankAccount bankAccount = marketAccount.getDealer().getCompanyBankAccount();

        marketWithdrawService.approveWithdraw(withdraw, bankAccount, reviewerUser);
    }

    public void denyWithdraw(BaseCompany company, String token, BaseUser reviewerUser, String denyReason) {

        BaseMarketWithdraw withdraw = getWithdrawAccount(company, token);
        marketWithdrawService.denyWithdraw(withdraw, reviewerUser, denyReason);

        // Envia o alerta de negacao de saque
        alertingService.dispatchWithdrawAlert(withdraw, MediaTypeEnum.EMAIL, AlertTypeConstants.MARKET_WITHDRAW_DENY);
    }

    public void releaseWithdraw(BaseCompany company, String token) {

        BaseMarketWithdraw withdraw = getWithdrawAccount(company, token);
        marketWithdrawService.releaseWithdraw(withdraw);

        // Envia o alerta de liberacao de saque
        alertingService.dispatchWithdrawAlert(withdraw, MediaTypeEnum.EMAIL, AlertTypeConstants.MARKET_WITHDRAW_RELEASE);
    }

    public void cancelWithdraw(BaseDealer dealer, String token) {

        BaseDealerMarketAccount marketAccount = getActiveAccount(dealer);
        marketWithdrawService.cancelWithdraw(marketAccount, token);
    }

}
