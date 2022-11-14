package tech.jannotti.billing.core.persistence.repository.base.user;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.user.BaseUser;
import tech.jannotti.billing.core.persistence.repository.AbstractRepository;

@Repository
public interface UserRepository extends AbstractRepository<BaseUser, Long> {

    public BaseUser getByUsername(String username);

    public boolean existsByUsername(String username);

    public boolean existsByUsernameAndTokenNot(String username, String token);

    @Modifying
    @Query("UPDATE BaseUser SET status=:status WHERE id=:id")
    public void updateStatusById(@Param("id") long id, @Param("status") EntityStatusEnum status);

    @Modifying
    @Query("UPDATE BaseUser SET status=:status, deletionDate=:deletionDate WHERE id=:id")
    public void updateStatusAndDeletionDateById(@Param("id") long id, @Param("status") EntityStatusEnum status,
        @Param("deletionDate") LocalDateTime deletionDate);

    @Modifying
    @Query("UPDATE BaseUser SET password=:password WHERE id=:id")
    public void updatePasswordById(@Param("id") long id, @Param("password") String password);

}