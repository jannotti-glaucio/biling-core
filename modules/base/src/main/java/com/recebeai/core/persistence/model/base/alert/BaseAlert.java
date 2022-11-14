package tech.jannotti.billing.core.persistence.model.base.alert;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.enums.AlertStatusEnum;
import tech.jannotti.billing.core.persistence.enums.MediaTypeEnum;
import tech.jannotti.billing.core.persistence.enums.converters.AlertStatusConverter;
import tech.jannotti.billing.core.persistence.enums.converters.MediaTypeConverter;
import tech.jannotti.billing.core.persistence.model.AbstractModel;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = "base", name = "alert")
public abstract class BaseAlert extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "media_type")
    @Convert(converter = MediaTypeConverter.class)
    protected MediaTypeEnum mediaType;

    @OneToOne
    @JoinColumn(name = "alert_type_id")
    protected BaseAlertType alertType;

    @Column(name = "status")
    @Convert(converter = AlertStatusConverter.class)
    private AlertStatusEnum status;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "request_date")
    protected LocalDateTime requestDate;

    @Column(name = "response_date")
    protected LocalDateTime responseDate;

    @OneToOne
    @JoinColumn(name = "result_code_id")
    protected BaseResultCode resultCode;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id);
    }

}
