package tech.jannotti.billing.core.persistence.repository.base.customer;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.customer.BaseCustomer;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface CustomerRepository extends AbstractRepository<BaseCustomer, Long> {

    public Page<BaseCustomer> findByDealerAndStatusNot(BaseDealer dealer, EntityStatusEnum status, Pageable pageable);

    @Query(value = "SELECT c FROM BaseCustomer c WHERE c.dealer=:dealer"
        + " AND (LOWER(c.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(c.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(c.email) LIKE LOWER(CONCAT('%',:filter,'%')) )"
        + " AND c.status != :status")
    public Page<BaseCustomer> findByDealerAndFilterAndStatusNot(@Param("dealer") BaseDealer dealer,
        @Param("filter") String filter, @Param("status") EntityStatusEnum status, Pageable pageable);

    public BaseCustomer getByDealerAndTokenAndStatusNot(BaseDealer dealer, String token, EntityStatusEnum status);

    public boolean existsByToken(String token);

    @Modifying
    @Query("UPDATE BaseCustomer SET status=:status, deletionDate=:deletionDate WHERE id=:id")
    public void updateStatusAndDeletionDateById(@Param("id") long id, @Param("status") EntityStatusEnum status,
        @Param("deletionDate") LocalDateTime deletionDate);

}
