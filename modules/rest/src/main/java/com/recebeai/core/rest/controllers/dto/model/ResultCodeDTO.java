package tech.jannotti.billing.core.rest.controllers.dto.model;

import org.apache.commons.lang.ArrayUtils;

import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResultCodeDTO extends AbstractModelDTO {

    private boolean success;
    private String code;
    private String message;

    public ResultCodeDTO(BaseResultCode resultCode, String... parameters) {
        this.success = resultCode.isSuccess();
        this.code = resultCode.getKey();

        if (ArrayUtils.isNotEmpty(parameters)) {
            Object[] args = (Object[]) parameters;
            this.message = String.format(resultCode.getMessage(), args);
        } else
            this.message = resultCode.getMessage();
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("success", success)
            .add("code", code)
            .add("message", message);
    }

}
