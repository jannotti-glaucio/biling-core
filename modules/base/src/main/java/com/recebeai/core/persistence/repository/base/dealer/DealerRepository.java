package tech.jannotti.billing.core.persistence.repository.base.dealer;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBillingPlan;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface DealerRepository extends AbstractRepository<BaseDealer, Long> {

    public Page<BaseDealer> findByCompanyAndStatusNot(BaseCompany company, EntityStatusEnum status, Pageable pageable);

    public List<BaseDealer> findByCompanyAndStatusNot(BaseCompany company, EntityStatusEnum status);

    public Page<BaseDealer> findByCompanyAndBillingPlanAndStatusNot(BaseCompany company, BaseCompanyBillingPlan billingPlan,
        EntityStatusEnum status, Pageable pageable);

    public BaseDealer getByTokenAndStatusNot(String token, EntityStatusEnum status);

    public BaseDealer getByCompanyAndTokenAndStatusNot(BaseCompany company, String token, EntityStatusEnum status);

    @Query(value = "SELECT d FROM BaseDealer d WHERE d.company=:company"
        + " AND (LOWER(d.name) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(d.documentNumber) LIKE LOWER(CONCAT('%',:filter,'%'))"
        + " OR LOWER(d.email) LIKE LOWER(CONCAT('%',:filter,'%')) )"
        + " AND d.status != :status")
    public Page<BaseDealer> findByCompanyAndFilterLikeAndStatusNot(@Param("company") BaseCompany company,
        @Param("filter") String filter, @Param("status") EntityStatusEnum status, Pageable pageable);

    public boolean existsByToken(String token);

    @Modifying
    @Query("UPDATE BaseDealer SET status=:status, deletionDate=:deletionDate WHERE id=:id")
    public void updateStatusAndDeletionDateById(@Param("id") long id, @Param("status") EntityStatusEnum status,
        @Param("deletionDate") LocalDateTime deletionDate);

    public boolean existsByCompanyAndBillingPlanAndStatusNot(@Param("company") BaseCompany company,
        @Param("billingPlan") BaseCompanyBillingPlan billingPlan, @Param("status") EntityStatusEnum status);
}
