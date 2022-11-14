package tech.jannotti.billing.core.api.web.controllers.dto.response.invoice;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;

import tech.jannotti.billing.core.api.web.controllers.dto.mapper.RestDTOMapper;
import tech.jannotti.billing.core.api.web.controllers.dto.model.invoice.InvoiceDTO;
import tech.jannotti.billing.core.commons.bean.ToStringHelper;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;

import lombok.Getter;

@Getter
public class FindInvoicesRestResponse extends RestResponseDTO {

    private List<InvoiceDTO> invoices;
    private Integer totalPaidAmount;
    private Integer totalFees;
    private Integer totalNetAmount;

    public FindInvoicesRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, Page<BaseInvoice> page,
        int totalPaidAmount, int totalFees, int totalNetAmount) {
        super(page, resultCode);
        this.invoices = dtoMapper.mapList(page.getContent(), InvoiceDTO.class);

        this.totalPaidAmount = totalPaidAmount;
        this.totalFees = totalFees;
        this.totalNetAmount = totalNetAmount;
    }

    public FindInvoicesRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, Page<BaseInvoice> page) {
        super(page, resultCode);
        this.invoices = dtoMapper.mapList(page.getContent(), InvoiceDTO.class);
    }

    public FindInvoicesRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, List<BaseInvoice> invoices,
        int totalPaidAmount, int totalFees, int totalNetAmount) {
        super(resultCode);
        this.invoices = dtoMapper.mapList(invoices, InvoiceDTO.class);

        this.totalPaidAmount = totalPaidAmount;
        this.totalFees = totalFees;
        this.totalNetAmount = totalNetAmount;
    }

    public FindInvoicesRestResponse(BaseResultCode resultCode, RestDTOMapper dtoMapper, List<BaseInvoice> invoices) {
        super(resultCode);
        this.invoices = dtoMapper.mapList(invoices, InvoiceDTO.class);
    }

    @Override
    protected ToStringHelper buildToString() {
        return super.buildToString()
            .add("invoices.size", CollectionUtils.size(invoices))
            .add("totalPaidAmount", totalPaidAmount)
            .add("totalFees", totalFees)
            .add("totalNetAmount", totalNetAmount);
    }

}