package tech.jannotti.billing.core.persistence.repository.base.user;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.company.BaseCompany;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.user.BaseDealerUser;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface DealerUserRepository extends AbstractRepository<BaseDealerUser, Long> {

    @Query(value = "SELECT u FROM BaseDealerUser u WHERE u.dealer=:dealer"
        + " AND u.status != :status")
    public List<BaseDealerUser> findByDealerAndFilterAndStatusNot(@Param("dealer") BaseDealer dealer,
        @Param("status") EntityStatusEnum status);

    public BaseDealerUser getByDealerCompanyAndToken(BaseCompany comppany, String token);

}
