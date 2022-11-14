package tech.jannotti.billing.core.services.dealer;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.enums.CRUDOperationEnum;
import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.persistence.enums.EntityChildStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealerAddress;
import tech.jannotti.billing.core.persistence.repository.base.dealer.DealerAddressRepository;
import tech.jannotti.billing.core.services.AbstractService;
import tech.jannotti.billing.core.services.dto.request.address.AddressServiceRequest;
import tech.jannotti.billing.core.services.dto.request.address.UpdateAddressServiceRequest;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;

@Service
public class DealerAddressService extends AbstractService {

    @Autowired
    private DealerAddressRepository addressRepository;

    List<BaseDealerAddress> findActiveAddresses(BaseDealer dealer) {
        return addressRepository.findByDealerAndStatus(dealer, EntityChildStatusEnum.ACTIVE);
    }

    @Transactional
    void addAddresses(BaseDealer dealer, List<AddressServiceRequest> requestAddresses) {

        int billingAddresses = countBillingAddressToAdd(requestAddresses);
        if (billingAddresses == 0)
            throw new ResultCodeServiceException(CODE_ONE_BILLING_ADDRESS_REQUIRED);
        if (billingAddresses > 1)
            throw new ResultCodeServiceException(CODE_DUPLICATED_BILLING_ADDRESS);

        if (CollectionUtils.isNotEmpty(requestAddresses)) {
            for (AddressServiceRequest address : requestAddresses)
                addAddress(dealer, address);
        }
    }

    private int countBillingAddressToAdd(List<AddressServiceRequest> requests) {
        int count = 0;
        for (AddressServiceRequest address : requests) {
            if (address.isBillingAddress())
                count += 1;
        }
        return count;
    }

    private void addAddress(BaseDealer dealer, AddressServiceRequest request) {

        BaseDealerAddress address = dtoMapper.map(request, BaseDealerAddress.class);
        address.setDealer(dealer);
        address.setToken(generateToken());
        address.setStatus(EntityChildStatusEnum.ACTIVE);
        addressRepository.save(address);
    }

    private String generateToken() {
        return tokenGenerator.generateRandomHexToken("dealer.address.token", 10);
    }

    @Transactional
    void updateAddresses(BaseDealer dealer, List<UpdateAddressServiceRequest> requests) {

        if (CollectionUtils.isEmpty(requests))
            return;

        // Separa as requisicoes de enderecos por tipo de operacao
        List<UpdateAddressServiceRequest> requestsToUpdate = filterUpdateAddressRequests(requests, CRUDOperationEnum.UPDATE);
        List<UpdateAddressServiceRequest> requestsToAdd = filterUpdateAddressRequests(requests, CRUDOperationEnum.ADD);
        List<UpdateAddressServiceRequest> requestsToDelete = filterUpdateAddressRequests(requests, CRUDOperationEnum.DELETE);

        // Primeiro exlclui os enderecos
        for (UpdateAddressServiceRequest request : requestsToDelete) {
            deleteAddress(dealer, request);
        }

        // Depois atualia os enderecos existentes
        for (UpdateAddressServiceRequest request : requestsToUpdate) {
            updateAddress(dealer, request);
        }

        // Por fim inclui os novos enderecos
        for (UpdateAddressServiceRequest request : requestsToAdd) {
            addAddress(dealer, request);
        }

        // Verifica se quebrou a regra de ter um endereco de cobranca
        int billingAddresses = addressRepository.countByDealerAndBillingAddressAndStatus(dealer, true,
            EntityChildStatusEnum.ACTIVE);
        if (billingAddresses == 0)
            throw new ResultCodeServiceException(CODE_ONE_BILLING_ADDRESS_REQUIRED);
        if (billingAddresses > 1)
            throw new ResultCodeServiceException(CODE_DUPLICATED_BILLING_ADDRESS);
    }

    @Transactional
    void deleteAddresses(List<BaseDealerAddress> addresses) {

        if (CollectionUtils.isNotEmpty(addresses)) {
            for (BaseDealerAddress address : addresses) {
                if (!address.getStatus().equals(EntityChildStatusEnum.DELETED))
                    deleteAddress(address);
            }
        }
    }

    private void updateAddress(BaseDealer dealer, UpdateAddressServiceRequest request) {

        BaseDealerAddress address = addressRepository.getByDealerAndToken(dealer, request.getToken());
        if (address == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_ADDRESS_TOKEN_NOT_FOUND, request.getToken());

        dtoMapper.map(request, address);
        addressRepository.save(address);
    }

    private void deleteAddress(BaseDealer dealer, UpdateAddressServiceRequest request) {

        BaseDealerAddress address = addressRepository.getByDealerAndTokenAndStatus(dealer, request.getToken(),
            EntityChildStatusEnum.ACTIVE);
        if (address == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_ADDRESS_TOKEN_NOT_FOUND, request.getToken());

        deleteAddress(address);
    }

    private void deleteAddress(BaseDealerAddress address) {
        addressRepository.updateStatusById(address.getId(), EntityChildStatusEnum.DELETED);
    }

}
