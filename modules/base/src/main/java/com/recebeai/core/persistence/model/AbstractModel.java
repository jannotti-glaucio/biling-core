package tech.jannotti.billing.core.persistence.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;

@MappedSuperclass
public abstract class AbstractModel implements Serializable {
    private static final long serialVersionUID = 1L;

    protected ToStringHelper buildToString() {
        return ToStringHelper.build(this);
    }

    @Override
    public String toString() {
        return buildToString()
            .toString();
    }

}
