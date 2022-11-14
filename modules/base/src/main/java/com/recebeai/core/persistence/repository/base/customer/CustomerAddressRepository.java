package tech.jannotti.billing.core.persistence.repository.base.customer;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.EntityChildStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomer;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomerAddress;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface CustomerAddressRepository extends AbstractRepository<BaseCustomerAddress, Long> {

    public List<BaseCustomerAddress> findByCustomerAndStatus(BaseCustomer customer, EntityChildStatusEnum status);

    public BaseCustomerAddress getByCustomerAndToken(BaseCustomer customer, String token);

    public BaseCustomerAddress getByCustomerAndTokenAndStatus(BaseCustomer customer, String token, EntityChildStatusEnum status);

    public BaseCustomerAddress getByCustomerAndBillingAddressAndStatus(BaseCustomer customer, boolean billingAddress,
        EntityChildStatusEnum status);

    @Modifying
    @Query("UPDATE BaseCustomerAddress SET status=:status WHERE id=:id")
    public void updateStatusById(@Param("id") long id, @Param("status") EntityChildStatusEnum status);

    public int countByCustomerAndBillingAddressAndStatus(BaseCustomer customer, boolean billingAddress,
        EntityChildStatusEnum status);

}
