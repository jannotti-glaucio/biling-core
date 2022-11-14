package tech.jannotti.billing.core.persistence.repository.base.company;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompanyBillingPlan;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface CompanyBillingPlanRepository extends AbstractRepository<BaseCompanyBillingPlan, Integer> {

    public List<BaseCompanyBillingPlan> findByCompanyAndStatusNot(BaseCompany company, EntityStatusEnum status);

    public BaseCompanyBillingPlan getByCompanyAndToken(BaseCompany company, String token);

    public boolean existsByToken(String token);

    @Modifying
    @Query("UPDATE BaseCompanyBillingPlan SET status=:status, deletionDate=:deletionDate WHERE id=:id")
    public void updateStatusAndDeletionDateById(@Param("id") int id, @Param("status") EntityStatusEnum status,
        @Param("deletionDate") LocalDateTime deletionDate);

}
