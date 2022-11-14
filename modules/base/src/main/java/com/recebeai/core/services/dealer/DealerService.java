package tech.jannotti.billing.core.services.dealer;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBillingPlan;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealerAddress;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealerBankAccount;
import tech.jannotti.billing.core.persistence.repository.base.company.CompanyBillingPlanRepository;
import tech.jannotti.billing.core.persistence.repository.base.dealer.DealerRepository;
import tech.jannotti.billing.core.services.AbstractService;
import tech.jannotti.billing.core.services.DocumentTypeService;
import tech.jannotti.billing.core.services.dto.request.DealerServiceRequest;
import tech.jannotti.billing.core.services.dto.request.address.AddressServiceRequest;
import tech.jannotti.billing.core.services.dto.request.address.UpdateAddressServiceRequest;
import tech.jannotti.billing.core.services.dto.request.bank.BankAccountServiceRequest;
import tech.jannotti.billing.core.services.dto.request.bank.UpdateBankAccountServiceRequest;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;

@Service
public class DealerService extends AbstractService {

    @Autowired
    private DealerRepository dealerRepository;

    @Autowired
    private DealerAddressService addressService;

    @Autowired
    private DealerBankAccountService bankAccountService;

    @Autowired
    private DealerMarketService dealerMarketService;

    @Autowired
    private DocumentTypeService documentTypeService;

    @Autowired
    private CompanyBillingPlanRepository billingPlaneRepository;

    public Page<BaseDealer> find(BaseCompany company, String filter, Pageable pageable) {

        if (StringUtils.isNotBlank(filter))
            return dealerRepository.findByCompanyAndFilterLikeAndStatusNot(company, filter, EntityStatusEnum.DELETED, pageable);
        else
            return dealerRepository.findByCompanyAndStatusNot(company, EntityStatusEnum.DELETED, pageable);
    }

    public List<BaseDealer> find(BaseCompany company) {
        return dealerRepository.findByCompanyAndStatusNot(company, EntityStatusEnum.DELETED);
    }

    public Page<BaseDealer> findByBillingPlan(BaseCompany company, String billingPlaneToken, Pageable pageable) {
        BaseCompanyBillingPlan billingPlan = billingPlaneRepository.getByCompanyAndToken(company, billingPlaneToken);

        return dealerRepository.findByCompanyAndBillingPlanAndStatusNot(company, billingPlan, EntityStatusEnum.DELETED,
            pageable);
    }

    public BaseDealer getAndLoad(BaseCompany company, String token) {
        BaseDealer dealer = get(company, token);

        List<BaseDealerAddress> addresses = addressService.findActiveAddresses(dealer);
        List<BaseDealerBankAccount> dealerBankAccounts = bankAccountService.findActiveAccounts(dealer);

        dealer.setAddresses(addresses);
        dealer.setBankAccounts(dealerBankAccounts);

        return dealer;
    }

    public List<BaseDealerBankAccount> getBankAccounts(BaseDealer dealer) {
        List<BaseDealerBankAccount> bankAccounts = bankAccountService.findActiveAccounts(dealer);
        return bankAccounts;
    }

    public BaseDealer get(BaseCompany company, String token) {
        BaseDealer dealer = dealerRepository.getByCompanyAndTokenAndStatusNot(company, token, EntityStatusEnum.DELETED);
        if (dealer == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_TOKEN_NOT_FOUND, token);

        return dealer;
    }

    public BaseDealer getOrNull(BaseCompany company, String token) {
        return dealerRepository.getByCompanyAndTokenAndStatusNot(company, token, EntityStatusEnum.DELETED);
    }

    @Transactional
    public BaseDealer add(BaseCompany company, DealerServiceRequest request, List<AddressServiceRequest> requestAddresses,
        List<BankAccountServiceRequest> requestAccounts) {

        BaseDealer dealer = dtoMapper.map(request, BaseDealer.class);

        // Valida o numero do documento
        if (!documentTypeService.validate(dealer.getDocumentType(), dealer.getDocumentNumber()))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_INVALID_DOCUMENT_NUMBER, dealer.getDocumentNumber());

        dealer.setCompany(company);
        dealer.setToken(generateToken());
        dealer.setStatus(EntityStatusEnum.ACTIVE);
        dealer.setCreationDate(DateTimeHelper.getNowDateTime());
        dealerRepository.save(dealer);

        // Adiciona os enderecos
        addressService.addAddresses(dealer, requestAddresses);

        // Adiciona as contas bancarias
        bankAccountService.addAccounts(dealer, requestAccounts);

        // Cria uma conta virtual
        dealerMarketService.add(dealer);

        return dealer;
    }

    private String generateToken() {
        return tokenGenerator.generateRandomHexToken("dealer.token", 8);
    }

    @Transactional
    public void update(BaseCompany company, String token, DealerServiceRequest request,
        List<UpdateAddressServiceRequest> requestAddresses, List<UpdateBankAccountServiceRequest> requestBankAccounts) {

        BaseDealer dealer = get(company, token);

        if (!dealer.getStatus().equals(EntityStatusEnum.ACTIVE))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_INVALID_DEALER_STATUS_TO_UPDATE, token);

        dtoMapper.map(request, dealer);
        dealerRepository.save(dealer);

        // Atualiza os enderecos
        addressService.updateAddresses(dealer, requestAddresses);

        // Atualiza os enderecos
        bankAccountService.updateAccounts(dealer, requestBankAccounts);
    }

    @Transactional
    public void delete(BaseCompany company, String token) {

        BaseDealer dealer = getAndLoad(company, token);

        if (dealer.getStatus().equals(EntityStatusEnum.DELETED))
            throw new ResultCodeServiceException(CODE_INVALID_DEALER_STATUS_TO_DELETE);

        LocalDateTime deletionDate = DateTimeHelper.getNowDateTime();
        dealerRepository.updateStatusAndDeletionDateById(dealer.getId(), EntityStatusEnum.DELETED, deletionDate);

        addressService.deleteAddresses(dealer.getAddresses());
    }

}
