package tech.jannotti.billing.core.persistence.repository.base.alert;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import tech.jannotti.billing.core.persistence.enums.AlertStatusEnum;
import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

@NoRepositoryBean
public interface AbstractAlertRepository<T extends AbstractModel> extends JpaRepository<T, Long> {

    @Modifying
    @Query("UPDATE BaseAlert SET status=:status, requestDate=:requestDate, responseDate=:responseDate,"
        + " resultCode=:resultCode WHERE id=:id")
    public void updateStatusAndRequestDateAndResponseDateAndResultCodeById(@Param("id") long id,
        @Param("status") AlertStatusEnum status, @Param("requestDate") LocalDateTime requestDate,
        @Param("responseDate") LocalDateTime responseDate, @Param("resultCode") BaseResultCode resultCode);
}
