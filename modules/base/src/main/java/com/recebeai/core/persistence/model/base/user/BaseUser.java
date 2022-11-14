package tech.jannotti.billing.core.persistence.model.base.user;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.enums.converters.EntityStatusConverter;
import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.BaseRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = "base", name = "user")
public class BaseUser extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "token")
    protected String token;

    @Column(name = "username")
    protected String username;

    @Column(name = "email")
    protected String email;

    @Column(name = "real_name")
    protected String realName;

    @Column(name = "password")
    protected String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    protected BaseRole role;

    @Column(name = "status")
    @Convert(converter = EntityStatusConverter.class)
    protected EntityStatusEnum status;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "deletion_date")
    private LocalDateTime deletionDate;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id)
            .add("token", token)
            .add("username", username);
    }

}
