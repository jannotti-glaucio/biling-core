package tech.jannotti.billing.core.services.customer;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.enums.CRUDOperationEnum;
import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.persistence.enums.EntityChildStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomer;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomerAddress;
import tech.jannotti.billing.core.persistence.repository.base.customer.CustomerAddressRepository;
import tech.jannotti.billing.core.services.AbstractService;
import tech.jannotti.billing.core.services.dto.request.address.AddressServiceRequest;
import tech.jannotti.billing.core.services.dto.request.address.UpdateAddressServiceRequest;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;

@Service
public class CustomerAddressService extends AbstractService {

    // TODO Ver se tem como criar uma heranca com DealerAddressService

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    List<BaseCustomerAddress> findActiveAddresses(BaseCustomer customer) {
        return customerAddressRepository.findByCustomerAndStatus(customer, EntityChildStatusEnum.ACTIVE);
    }

    BaseCustomerAddress getBillingAddress(BaseCustomer customer) {

        BaseCustomerAddress customerAddress = customerAddressRepository.getByCustomerAndBillingAddressAndStatus(customer, true,
            EntityChildStatusEnum.ACTIVE);
        if (customerAddress == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_CUSTOMER_BILLING_ADDRESS_REQUIRED);
        else
            return customerAddress;
    }

    @Transactional
    void addAddresses(BaseCustomer customer, List<AddressServiceRequest> requestAddresses) {

        int billingAddresses = countBillingAddressToAdd(requestAddresses);
        if (billingAddresses == 0)
            throw new ResultCodeServiceException(CODE_ONE_BILLING_ADDRESS_REQUIRED);
        if (billingAddresses > 1)
            throw new ResultCodeServiceException(CODE_DUPLICATED_BILLING_ADDRESS);

        if (CollectionUtils.isNotEmpty(requestAddresses)) {
            for (AddressServiceRequest address : requestAddresses)
                addAddress(customer, address);
        }
    }

    @Transactional
    void updateAddresses(BaseCustomer customer, List<UpdateAddressServiceRequest> requests) {

        if (CollectionUtils.isEmpty(requests))
            return;

        // Separa as requisicoes de enderecos por tipo de operacao
        List<UpdateAddressServiceRequest> requestsToUpdate = filterUpdateAddressRequests(requests, CRUDOperationEnum.UPDATE);
        List<UpdateAddressServiceRequest> requestsToAdd = filterUpdateAddressRequests(requests, CRUDOperationEnum.ADD);
        List<UpdateAddressServiceRequest> requestsToDelete = filterUpdateAddressRequests(requests, CRUDOperationEnum.DELETE);

        // Primeiro exlclui os enderecos
        for (UpdateAddressServiceRequest request : requestsToDelete) {
            deleteAddress(customer, request);
        }

        // Depois atualia os enderecos existentes
        for (UpdateAddressServiceRequest request : requestsToUpdate) {
            updateAddress(customer, request);
        }

        // Por fim inclui os novos enderecos
        for (UpdateAddressServiceRequest request : requestsToAdd) {
            addAddress(customer, request);
        }

        // Verifica se quebrou a regra de ter um endereco de cobranca
        int billingAddresses = customerAddressRepository.countByCustomerAndBillingAddressAndStatus(customer, true,
            EntityChildStatusEnum.ACTIVE);
        if (billingAddresses == 0)
            throw new ResultCodeServiceException(CODE_ONE_BILLING_ADDRESS_REQUIRED);
        if (billingAddresses > 1)
            throw new ResultCodeServiceException(CODE_DUPLICATED_BILLING_ADDRESS);
    }

    @Transactional
    void deleteAddresses(List<BaseCustomerAddress> addresses) {

        if (CollectionUtils.isNotEmpty(addresses)) {
            for (BaseCustomerAddress address : addresses) {
                if (!address.getStatus().equals(EntityChildStatusEnum.DELETED))
                    deleteAddress(address);
            }
        }
    }

    @Transactional
    void updateAddress(BaseCustomer customer, AddressServiceRequest request) {

        BaseCustomerAddress address = getBillingAddress(customer);

        dtoMapper.map(request, address);
        customerAddressRepository.save(address);
    }

    @Transactional
    void addAddress(BaseCustomer customer, AddressServiceRequest request) {

        BaseCustomerAddress address = dtoMapper.map(request, BaseCustomerAddress.class);
        address.setCustomer(customer);
        address.setToken(generateAddressToken());
        address.setStatus(EntityChildStatusEnum.ACTIVE);
        customerAddressRepository.save(address);
    }

    private void updateAddress(BaseCustomer customer, UpdateAddressServiceRequest request) {

        BaseCustomerAddress address = customerAddressRepository.getByCustomerAndToken(customer, request.getToken());
        if (address == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_ADDRESS_TOKEN_NOT_FOUND, request.getToken());

        dtoMapper.map(request, address);
        customerAddressRepository.save(address);
    }

    private void deleteAddress(BaseCustomer customer, UpdateAddressServiceRequest request) {

        BaseCustomerAddress address = customerAddressRepository.getByCustomerAndTokenAndStatus(customer, request.getToken(),
            EntityChildStatusEnum.ACTIVE);
        if (address == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_ADDRESS_TOKEN_NOT_FOUND, request.getToken());

        deleteAddress(address);
    }

    private void deleteAddress(BaseCustomerAddress address) {
        customerAddressRepository.updateStatusById(address.getId(), EntityChildStatusEnum.DELETED);
    }

    private String generateAddressToken() {
        return tokenGenerator.generateRandomHexToken("customer.address.token", 14);
    }

    private int countBillingAddressToAdd(List<AddressServiceRequest> requests) {
        int count = 0;
        for (AddressServiceRequest address : requests) {
            if (address.isBillingAddress())
                count += 1;
        }
        return count;
    }

}
