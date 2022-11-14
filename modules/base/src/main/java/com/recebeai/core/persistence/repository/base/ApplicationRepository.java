package tech.jannotti.billing.core.persistence.repository.base;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseApplication;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface ApplicationRepository extends AbstractRepository<BaseApplication, Long> {

    public List<BaseApplication> findByDealerAndStatusNot(BaseDealer dealer, EntityStatusEnum status);

    public BaseApplication getByDealerAndTokenAndStatusNot(BaseDealer dealer, String token, EntityStatusEnum status);

    public BaseApplication getByClientId(String clientId);

    @Modifying
    @Query("UPDATE BaseApplication SET clientSecret=:clientSecret WHERE id=:id")
    public void updateClientSecretById(@Param("id") long id, @Param("clientSecret") String clientSecret);

    @Modifying
    @Query("UPDATE BaseApplication SET status=:status, deletionDate=:deletionDate WHERE id=:id")
    public void updateStatusAndDeletionDateById(@Param("id") long id, @Param("status") EntityStatusEnum status,
        @Param("deletionDate") LocalDateTime deletionDate);

}