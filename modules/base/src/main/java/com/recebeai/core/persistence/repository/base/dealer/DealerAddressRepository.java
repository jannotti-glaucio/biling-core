package tech.jannotti.billing.core.persistence.repository.base.dealer;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.EntityChildStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealerAddress;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface DealerAddressRepository extends AbstractRepository<BaseDealerAddress, Long> {
	
	public List<BaseDealerAddress> findByDealerAndStatus(BaseDealer dealer, EntityChildStatusEnum status);
	 
	public BaseDealerAddress getByDealerAndToken(BaseDealer dealer, String token);

    public BaseDealerAddress getByDealerAndTokenAndStatus(BaseDealer dealer, String token, EntityChildStatusEnum status);

    public BaseDealerAddress getByDealerAndBillingAddress(BaseDealer dealer, boolean billingAddress);

    @Modifying
    @Query("UPDATE BaseDealerAddress SET status=:status WHERE id=:id")
    public void updateStatusById(@Param("id") long id, @Param("status") EntityChildStatusEnum status);

    public int countByDealerAndBillingAddressAndStatus(BaseDealer dealer, boolean billingAddress,
        EntityChildStatusEnum status);

}