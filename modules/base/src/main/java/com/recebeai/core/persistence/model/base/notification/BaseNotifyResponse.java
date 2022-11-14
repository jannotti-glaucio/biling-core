package tech.jannotti.billing.core.persistence.model.base.notification;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.AbstractModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "notify_response")
public class BaseNotifyResponse extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "notify_request_id")
    private BaseNotifyRequest notifyRequest;

    @Column(name = "response_date")
    private LocalDateTime responseDate;

    @Column(name = "http_status")
    private Integer httpStatus;

    @Column(name = "exception_message")
    private String exceptionMessage;

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id);
    }

}
