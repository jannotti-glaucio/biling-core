package tech.jannotti.billing.core.persistence.model.base.resultcode;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.constants.CoreConstants;
import tech.jannotti.billing.core.persistence.model.AbstractModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "base", name = "result_code")
public class BaseResultCode extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Transient
    private LogManager logManager = LogFactory.getManager(BaseResultCode.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "key")
    private String key;

    @Column(name = "name")
    private String name;

    @Column(name = "success")
    private boolean success;

    @ManyToOne
    @JoinColumn(name = "result_code_group_id")
    private BaseResultCodeGroup group;

    @Column(name = "http_status")
    private Integer httpStatus;

    @OneToMany(mappedBy = "resultCode")
    private List<BaseResultCodeMessage> messages;

    public Integer getCurrentHttpStatus() {
        if (group != null)
            return group.getHttpStatus();
        else
            return httpStatus;
    }

    public String getMessage() {
        return getMessage(CoreConstants.DEFAULT_LANGUAGE_CODE);
    }

    public String getMessage(String languageCode) {

        if (CollectionUtils.isNotEmpty(messages)) {
            for (BaseResultCodeMessage message : messages) {
                if (message.getLanguague().getCode().equals(languageCode))
                    return message.getMessage();
            }
        }

        logManager.logWARN("Mensagem nao localizada para o idioma [%s]", languageCode);
        return null;
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("id", id)
            .add("key", key);
    }

}
