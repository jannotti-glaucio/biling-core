package tech.jannotti.billing.core.persistence.model.base;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.enums.converters.EntityStatusConverter;
import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "application")
public class BaseApplication extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dealer_id")
    private BaseDealer dealer;

    @Column(name = "token")
    private String token;

    @Column(name = "name")
    private String name;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Transient
    private String plainClientSecret;

    @Column(name = "status")
    @Convert(converter = EntityStatusConverter.class)
    private EntityStatusEnum status;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "deletion_date")
    private LocalDateTime deletionDate;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id)
            .add("token", token);
    }

}
