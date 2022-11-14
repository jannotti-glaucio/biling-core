package tech.jannotti.billing.core.api.web.controllers.dto.mapper;

import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.mapping.ToHelpVideoDTOMapping;
import tech.jannotti.billing.core.api.web.controllers.dto.mapper.mapping.ToInvoiceDTOMapping;
import tech.jannotti.billing.core.api.web.controllers.dto.mapper.mapping.ToInvoiceShortDTOMapping;
import tech.jannotti.billing.core.api.web.controllers.dto.mapper.mapping.ToMarketStatementTypeDTOMapping;
import tech.jannotti.billing.core.api.web.controllers.dto.mapper.mapping.ToPaymentShortDTOMapping;
import tech.jannotti.billing.core.rest.controllers.dto.mapper.AbstractRestDTOMapper;

@Component
public class RestDTOMapper extends AbstractRestDTOMapper {

    @Override
    protected void configure() {
        super.configure();
        configureMappings();
    }

    private void configureMappings() {

        modelMapper.addMappings(new ToHelpVideoDTOMapping());
        modelMapper.addMappings(new ToInvoiceDTOMapping());
        modelMapper.addMappings(new ToInvoiceShortDTOMapping());
        modelMapper.addMappings(new ToMarketStatementTypeDTOMapping());
        modelMapper.addMappings(new ToPaymentShortDTOMapping());
    }

}
