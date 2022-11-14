package tech.jannotti.billing.core.rest.controllers.dto.mapper;

import tech.jannotti.billing.core.commons.bean.AbstractBeanMapper;
import tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.from.FromDocumentTypeConverter;
import tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.from.FromStateConverter;
import tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.to.ToBankConverter;
import tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.to.ToCompanyBankAccountConverter;
import tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.to.ToCompanyBillingPlanConverter;
import tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.to.ToCustomerConverter;
import tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.to.ToDealerBankAccountConverter;
import tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.model.to.ToDocumentTypeConverter;
import tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.type.ToLocalDateConverter;
import tech.jannotti.billing.core.rest.controllers.dto.mapper.converter.type.ToLocalDateTimeConverter;

public class AbstractRestDTOMapper extends AbstractBeanMapper {

    @Override
    protected void configure() {
        configureConverters();
    }

    private void configureConverters() {

        // from DTO do type converters
        modelMapper.addConverter(new ToLocalDateConverter());
        modelMapper.addConverter(new ToLocalDateTimeConverter());

        // from model to DTO converters
        modelMapper.addConverter(new FromDocumentTypeConverter());
        modelMapper.addConverter(new FromStateConverter());

        // from DTO to model converters
        modelMapper.addConverter(getManagedConverter(ToDocumentTypeConverter.class));
        modelMapper.addConverter(getManagedConverter(ToCustomerConverter.class));

        modelMapper.addConverter(getManagedConverter(ToBankConverter.class));
        modelMapper.addConverter(getManagedConverter(ToCompanyBankAccountConverter.class));
        modelMapper.addConverter(getManagedConverter(ToDealerBankAccountConverter.class));

        modelMapper.addConverter(getManagedConverter(ToCompanyBillingPlanConverter.class));
    }

}
