package tech.jannotti.billing.core.persistence.repository.base.transfer;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import tech.jannotti.billing.core.persistence.enums.TransferStatusEnum;
import tech.jannotti.billing.core.persistence.model.AbstractModel;

@NoRepositoryBean
public interface AbstractTransferRepository<T extends AbstractModel> extends JpaRepository<T, Long> {

    @Modifying
    @Query("UPDATE BaseTransfer SET status=:status, processingDate=:processingDate WHERE id=:id")
    public void updateStatusAndProcessingDateById(@Param("id") long id, @Param("status") TransferStatusEnum status,
        @Param("processingDate") LocalDateTime processingDate);

}
