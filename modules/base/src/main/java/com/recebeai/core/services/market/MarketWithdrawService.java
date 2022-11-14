package tech.jannotti.billing.core.services.market;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.constants.enums.MarketStatamentTypeConstants;
import tech.jannotti.billing.core.persistence.enums.MarketWithdrawStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBankAccount;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketAccount;
import tech.jannotti.billing.core.persistence.model.base.market.BaseMarketWithdraw;
import tech.jannotti.billing.core.persistence.model.base.user.BaseUser;
import tech.jannotti.billing.core.persistence.repository.base.market.MarketWithdrawRepository;
import tech.jannotti.billing.core.services.dto.request.MarketWithdrawServiceRequest;
import tech.jannotti.billing.core.services.dto.response.market.SummarizeMarketWithdrawsServiceResponse;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;
import tech.jannotti.billing.core.services.transfer.TransferBankAccountService;

@Service
public class MarketWithdrawService extends AbstractMarketService {

    @Autowired
    private MarketWithdrawRepository marketWithdrawRepository;

    @Autowired
    private TransferBankAccountService transferBankAccountService;

    @Autowired
    private MarketAccountService marketAccountService;

    @Autowired
    private MarketStatementService marketStatementService;

    public Page<BaseMarketWithdraw> findWithdraws(BaseMarketAccount marketAccount, LocalDate startDate, LocalDate endDate,
        Pageable pageable) {
        return marketWithdrawRepository.findByMarketAccountAndRequestDateBetween(marketAccount, startDate, endDate, pageable);
    }

    public Page<BaseMarketWithdraw> findWithdraws(List<? extends BaseMarketAccount> marketAccounts, LocalDate startDate,
        LocalDate endDate, MarketWithdrawStatusEnum[] status, Pageable pageable) {

        if (ArrayUtils.isEmpty(status))
            return marketWithdrawRepository.findByMarketAccountInAndRequestDateBetween(marketAccounts, startDate, endDate,
                pageable);
        else
            return marketWithdrawRepository.findByMarketAccountInAndRequestDateBetweenAndStatusIn(marketAccounts, startDate,
                endDate, status, pageable);
    }

    public List<BaseMarketWithdraw> findWithdraws(List<? extends BaseMarketAccount> marketAccounts, LocalDate startDate,
        LocalDate endDate, MarketWithdrawStatusEnum[] status) {

        if (ArrayUtils.isEmpty(status))
            return marketWithdrawRepository.findByMarketAccountInAndRequestDateBetween(marketAccounts, startDate, endDate);
        else
            return marketWithdrawRepository.findByMarketAccountInAndRequestDateBetweenAndStatusIn(marketAccounts, startDate,
                endDate, status);
    }

    public SummarizeMarketWithdrawsServiceResponse summarizeWithdraws(List<? extends BaseMarketAccount> marketAccounts,
        LocalDate startDate, LocalDate endDate, MarketWithdrawStatusEnum[] status) {

        Integer totalAmount = null;
        Integer totalFees = null;

        if (ArrayUtils.isEmpty(status)) {
            totalAmount = marketWithdrawRepository.sumAmountByMarketAccountInAndRequestDateBetween(marketAccounts, startDate,
                endDate);
            totalFees = marketWithdrawRepository.sumWithdrawFeeByMarketAccountInAndRequestDateBetween(marketAccounts,
                startDate, endDate);
        } else {
            totalAmount = marketWithdrawRepository.sumAmountByMarketAccountInAndRequestDateBetweenAndStatusIn(marketAccounts,
                startDate, endDate, status);
            totalFees = marketWithdrawRepository.sumWithdrawFeeByMarketAccountInAndRequestDateBetweenAndStatusIn(
                marketAccounts, startDate, endDate, status);
        }

        SummarizeMarketWithdrawsServiceResponse response = new SummarizeMarketWithdrawsServiceResponse(totalAmount, totalFees);
        return response;
    }

    public Page<BaseMarketWithdraw> findPendingWithdraws(List<? extends BaseMarketAccount> marketAccounts,
        Pageable pageable) {
        MarketWithdrawStatusEnum[] status = { MarketWithdrawStatusEnum.REQUESTED, MarketWithdrawStatusEnum.APPROVED };
        return marketWithdrawRepository.findByMarketAccountInAndStatusIn(marketAccounts, status, pageable);
    }

    public SummarizeMarketWithdrawsServiceResponse summarizePendingWithdraws(List<? extends BaseMarketAccount> marketAccounts) {
        MarketWithdrawStatusEnum[] status = { MarketWithdrawStatusEnum.REQUESTED, MarketWithdrawStatusEnum.APPROVED };

        Integer totalAmount = marketWithdrawRepository.sumAmountByMarketAccountInAndStatusIn(marketAccounts, status);
        Integer totalFees = marketWithdrawRepository.sumWithdrawFeeByMarketAccountInAndStatusIn(marketAccounts, status);

        SummarizeMarketWithdrawsServiceResponse response = new SummarizeMarketWithdrawsServiceResponse(totalAmount, totalFees);
        return response;
    }

    @Transactional
    public BaseMarketWithdraw addWithdraw(BaseMarketAccount marketAccount, BaseUser user, MarketWithdrawServiceRequest request,
        int withdrawFee) {

        long currentBalance = marketAccountService.getCurrentBalance(marketAccount);
        if (request.getAmount() > currentBalance)
            throw new ResultCodeServiceException(CODE_INSUFFICIENT_BALANCE_TO_REQUEST_WITHDRAW);

        LocalDateTime requestDate = DateTimeHelper.getNowDateTime();

        BaseMarketWithdraw withdraw = new BaseMarketWithdraw();
        withdraw.setAmount(request.getAmount());
        withdraw.setWithdrawFee(withdrawFee);
        withdraw.setBankAccount(request.getBankAccount());
        withdraw.setMarketAccount(marketAccount);
        withdraw.setToken(generateWithdrawToken());
        withdraw.setRequesterUser(user);
        withdraw.setRequestDate(requestDate);
        withdraw.setStatus(MarketWithdrawStatusEnum.REQUESTED);
        marketWithdrawRepository.save(withdraw);

        // Cria uma operacao de debito na conta
        marketStatementService.addStatement(withdraw.getMarketAccount(), MarketStatamentTypeConstants.WITHDRAW_RESERVE.getCode(),
            requestDate.toLocalDate(), withdraw.getAmount(), withdraw);

        return withdraw;
    }

    private String generateWithdrawToken() {
        return tokenGenerator.generateRandomHexToken("marketWithdraw.token", 20);
    }

    public BaseMarketWithdraw getWithdraw(String token) {
        return marketWithdrawRepository.findByToken(token);
    }

    @Transactional
    public void approveWithdraw(BaseMarketWithdraw withdraw, BaseCompanyBankAccount companyBankAccount, BaseUser reviewerUser) {

        if (!withdraw.getStatus().equals(MarketWithdrawStatusEnum.REQUESTED))
            throw new ResultCodeServiceException(CODE_INVALID_MARKET_WITHDRAW_STATUS_TO_APPROVE);

        // Atualiza o status do saque para aprovado
        LocalDateTime reviewDate = DateTimeHelper.getNowDateTime();
        withdraw.setStatus(MarketWithdrawStatusEnum.APPROVED);
        withdraw.setReviewDate(reviewDate);
        withdraw.setReviewerUser(reviewerUser);
        marketWithdrawRepository.save(withdraw);

        // Cria uma transferencia bancaria pendente
        transferBankAccountService.add(withdraw, reviewDate.toLocalDate(), companyBankAccount);
    }

    @Transactional
    public void denyWithdraw(BaseMarketWithdraw withdraw, BaseUser reviewerUser, String denyReason) {

        if (!withdraw.getStatus().equals(MarketWithdrawStatusEnum.REQUESTED))
            throw new ResultCodeServiceException(CODE_INVALID_MARKET_WITHDRAW_STATUS_TO_DENY);

        LocalDateTime reviewDate = DateTimeHelper.getNowDateTime();
        withdraw.setStatus(MarketWithdrawStatusEnum.DENIED);
        withdraw.setReviewDate(reviewDate);
        withdraw.setReviewerUser(reviewerUser);
        withdraw.setDenyReason(denyReason);
        marketWithdrawRepository.save(withdraw);

        // Cria uma operacao de credito na conta
        marketStatementService.addStatement(withdraw.getMarketAccount(), MarketStatamentTypeConstants.WITHDRAW_REFUND.getCode(),
            reviewDate.toLocalDate(), withdraw.getAmount(), withdraw);
    }

    @Transactional
    public void releaseWithdraw(BaseMarketWithdraw withdraw) {

        if (!withdraw.getStatus().equals(MarketWithdrawStatusEnum.APPROVED))
            throw new ResultCodeServiceException(CODE_INVALID_MARKET_WITHDRAW_STATUS_TO_PROVIDE);

        // Atualiza o status do saque para liberado
        LocalDateTime provideDate = DateTimeHelper.getNowDateTime();
        marketWithdrawRepository.updateStatusAndReleaseDateById(withdraw.getId(), MarketWithdrawStatusEnum.RELEASED,
            provideDate);

        // Conclui a transferencia bancaria pendente
        transferBankAccountService.done(withdraw);
    }

    @Transactional
    public void cancelWithdraw(BaseMarketAccount marketAccount, String token) {

        BaseMarketWithdraw withdraw = marketWithdrawRepository.findByMarketAccountAndToken(marketAccount, token);
        if (withdraw == null)
            throw new ResultCodeServiceException(CODE_TOKEN_NOT_FOUND, token);

        if (!withdraw.getStatus().equals(MarketWithdrawStatusEnum.REQUESTED))
            throw new ResultCodeServiceException(CODE_INVALID_MARKET_WITHDRAW_STATUS_TO_CANCEL);

        // Atualiza o status do saque para cancelado
        LocalDateTime cancelationDate = DateTimeHelper.getNowDateTime();
        marketWithdrawRepository.updateStatusAndCancelationDateById(withdraw.getId(), MarketWithdrawStatusEnum.CANCELED,
            cancelationDate);

        // Cria uma operacao de credito na conta
        marketStatementService.addStatement(withdraw.getMarketAccount(), MarketStatamentTypeConstants.WITHDRAW_REFUND.getCode(),
            cancelationDate.toLocalDate(), withdraw.getAmount(), withdraw);
    }

}
