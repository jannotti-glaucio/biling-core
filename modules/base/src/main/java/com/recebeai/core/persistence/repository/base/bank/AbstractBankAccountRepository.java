package tech.jannotti.billing.core.persistence.repository.base.bank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import tech.jannotti.billing.core.persistence.enums.EntityChildStatusEnum;
import tech.jannotti.billing.core.persistence.model.AbstractModel;

@NoRepositoryBean
public interface AbstractBankAccountRepository<T extends AbstractModel> extends JpaRepository<T, Long> {

    public boolean existsByToken(String token);

    @Modifying
    @Query("UPDATE BaseBankAccount SET status=:status WHERE id=:id")
    public void updateStatusById(@Param("id") long id, @Param("status") EntityChildStatusEnum status);

}
