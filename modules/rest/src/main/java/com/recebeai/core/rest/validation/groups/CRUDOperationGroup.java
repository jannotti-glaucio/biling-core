package tech.jannotti.billing.core.rest.validation.groups;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import tech.jannotti.billing.core.commons.enums.CRUDOperationEnum;
import tech.jannotti.billing.core.rest.controllers.dto.request.CRUDOperationRestRequest;
import tech.jannotti.billing.core.validation.extension.groups.AddValidations;
import tech.jannotti.billing.core.validation.extension.groups.DeleteValidations;
import tech.jannotti.billing.core.validation.extension.groups.UpdateValidations;

public abstract class CRUDOperationGroup<T extends CRUDOperationRestRequest>
    implements DefaultGroupSequenceProvider<T> {

    private Class<T> type;

    public CRUDOperationGroup(Class<T> type) {
        this.type = type;
    }

    @Override
    public List<Class<?>> getValidationGroups(T request) {
        List<Class<?>> sequence = new ArrayList<>();

        if ((request != null) && StringUtils.isNotBlank(request.getOperation())) {

            if (request.getOperation().equals(CRUDOperationEnum.ADD.toString()))
                sequence.add(AddValidations.class);
            else if (request.getOperation().equals(CRUDOperationEnum.UPDATE.toString()))
                sequence.add(UpdateValidations.class);
            else if (request.getOperation().equals(CRUDOperationEnum.DELETE.toString()))
                sequence.add(DeleteValidations.class);
        }

        sequence.add(type);
        return sequence;
    }

}
