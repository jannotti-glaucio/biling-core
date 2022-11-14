package tech.jannotti.billing.core.services.customer;

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
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomer;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomerAddress;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.repository.base.customer.CustomerRepository;
import tech.jannotti.billing.core.services.AbstractService;
import tech.jannotti.billing.core.services.DocumentTypeService;
import tech.jannotti.billing.core.services.dto.request.CustomerServiceRequest;
import tech.jannotti.billing.core.services.dto.request.address.AddressServiceRequest;
import tech.jannotti.billing.core.services.dto.request.address.UpdateAddressServiceRequest;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;

@Service
public class CustomerService extends AbstractService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerAddressService customerAddressService;

    @Autowired
    private DocumentTypeService documentTypeService;

    public Page<BaseCustomer> find(BaseDealer dealer, String filter, Pageable pageable) {
        if (StringUtils.isNotBlank(filter))
            return customerRepository.findByDealerAndFilterAndStatusNot(dealer, filter, EntityStatusEnum.DELETED, pageable);
        else
            return customerRepository.findByDealerAndStatusNot(dealer, EntityStatusEnum.DELETED, pageable);
    }

    public BaseCustomer getAndLoadAddresses(BaseDealer dealer, String token) {
        BaseCustomer customer = get(dealer, token);

        List<BaseCustomerAddress> addresses = customerAddressService.findActiveAddresses(customer);
        customer.setAddresses(addresses);

        return customer;
    }

    public BaseCustomer getAndLoadBillingAddress(BaseDealer dealer, String token) {
        BaseCustomer customer = get(dealer, token);

        BaseCustomerAddress billingAddresses = customerAddressService.getBillingAddress(customer);
        customer.setBillingAddress(billingAddresses);

        return customer;
    }

    public BaseCustomer get(BaseDealer dealer, String token) {
        BaseCustomer customer = customerRepository.getByDealerAndTokenAndStatusNot(dealer, token, EntityStatusEnum.DELETED);
        if (customer == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_TOKEN_NOT_FOUND, token);

        return customer;
    }

    public BaseCustomerAddress getBillingAddress(BaseCustomer customer) {

        BaseCustomerAddress address = customerAddressService.getBillingAddress(customer);
        if (address == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_CUSTOMER_BILLING_ADDRESS_REQUIRED);
        else
            return address;
    }

    public BaseCustomer add(BaseDealer dealer, CustomerServiceRequest request) {

        BaseCustomer customer = dtoMapper.map(request, BaseCustomer.class);

        // Valida o numero do documento
        if (!documentTypeService.validate(customer.getDocumentType(), customer.getDocumentNumber()))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_INVALID_DOCUMENT_NUMBER, customer.getDocumentNumber());

        customer.setDealer(dealer);
        customer.setToken(generateToken());
        customer.setStatus(EntityStatusEnum.ACTIVE);
        customer.setCreationDate(DateTimeHelper.getNowDateTime());
        customerRepository.save(customer);

        return customer;
    }

    @Transactional
    public BaseCustomer add(BaseDealer dealer, CustomerServiceRequest request, List<AddressServiceRequest> requestAddresses) {

        BaseCustomer customer = add(dealer, request);
        // Adiciona os enderecos
        customerAddressService.addAddresses(customer, requestAddresses);

        return customer;
    }

    @Transactional
    public BaseCustomer add(BaseDealer dealer, CustomerServiceRequest request, AddressServiceRequest requestBillingAddress) {

        BaseCustomer customer = add(dealer, request);

        // Adiciona o endereco de cobranca
        requestBillingAddress.setBillingAddress(true);
        customerAddressService.addAddress(customer, requestBillingAddress);

        return customer;
    }

    private String generateToken() {
        return tokenGenerator.generateRandomHexToken("customer.token", 12);
    }

    private BaseCustomer updateCustomer(BaseDealer dealer, String token, CustomerServiceRequest request) {

        BaseCustomer customer = get(dealer, token);

        if (!customer.getStatus().equals(EntityStatusEnum.ACTIVE))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_INVALID_CUSTOMER_STATUS_TO_UPDATE, token);

        dtoMapper.map(request, customer);
        customerRepository.save(customer);

        return customer;
    }

    @Transactional
    public void update(BaseDealer dealer, String token, CustomerServiceRequest request,
        List<UpdateAddressServiceRequest> requestAddresses) {

        BaseCustomer customer = updateCustomer(dealer, token, request);
        // Atualiza os enderecos
        customerAddressService.updateAddresses(customer, requestAddresses);
    }

    @Transactional
    public void update(BaseDealer dealer, String token, CustomerServiceRequest request,
        AddressServiceRequest requestBillingAddress) {

        BaseCustomer customer = updateCustomer(dealer, token, request);

        // Atualiza o endereco de cobranca
        requestBillingAddress.setBillingAddress(true);
        customerAddressService.updateAddress(customer, requestBillingAddress);
    }

    @Transactional
    public void delete(BaseDealer dealer, String token) {

        BaseCustomer customer = getAndLoadAddresses(dealer, token);

        if (customer.getStatus().equals(EntityStatusEnum.DELETED))
            throw new ResultCodeServiceException(CODE_INVALID_DEALER_STATUS_TO_DELETE);

        LocalDateTime deletionDate = DateTimeHelper.getNowDateTime();
        customerRepository.updateStatusAndDeletionDateById(customer.getId(), EntityStatusEnum.DELETED, deletionDate);

        customerAddressService.deleteAddresses(customer.getAddresses());
    }

}
