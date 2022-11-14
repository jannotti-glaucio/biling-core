package tech.jannotti.billing.core.api.web.controllers.dealer;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.api.web.controllers.AbstractWebController;
import tech.jannotti.billing.core.api.web.controllers.dto.request.order.collection.AddCollectionRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.request.order.collection.GetCollectionInstalmentsRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.response.order.collection.AddCollectionRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.order.collection.FindCollectionsRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.order.collection.GetCollectionInstalmentsRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.order.collection.GetCollectionRestResponse;
import tech.jannotti.billing.core.commons.enums.InvoiceDateToFilterEnum;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.persistence.enums.CollectionStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.model.base.order.BaseOrderCollection;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.rest.validation.ValidateBody;
import tech.jannotti.billing.core.rest.validation.ValidateParameters;
import tech.jannotti.billing.core.services.dto.model.OrderInstalmentServiceDTO;
import tech.jannotti.billing.core.services.dto.request.order.collection.CollectionServiceRequest;
import tech.jannotti.billing.core.services.dto.request.order.collection.GetCollectionInstalmentsServiceRequest;
import tech.jannotti.billing.core.services.order.CollectionService;
import tech.jannotti.billing.core.validation.extension.annotations.FilterLength;
import tech.jannotti.billing.core.validation.extension.annotations.NotBlankParameter;
import tech.jannotti.billing.core.validation.extension.annotations.ValidDate;
import tech.jannotti.billing.core.validation.extension.annotations.ValidInteger;
import tech.jannotti.billing.core.validation.extension.annotations.enums.ValidInvoiceDateToFilter;
import tech.jannotti.billing.core.validation.extension.annotations.enums.order.ValidCollectionStatusList;

@RestController("dealer.collectionController")
@RequestMapping(ApiConstants.V1_API_PATH + "dealer/collection")
public class CollectionController extends AbstractWebController {

    @Autowired
    private CollectionService collectionService;

    @GetMapping("/")
    @InfoLogging
    @ValidateParameters
    public FindCollectionsRestResponse find(
        @RequestParam(name = "startDate", required = false) @NotBlankParameter @ValidDate String startDate,
        @RequestParam(name = "endDate", required = false) @NotBlankParameter @ValidDate String endDate,
        @RequestParam(name = "dateToFilter", required = false) @ValidInvoiceDateToFilter String dateToFilter,
        @RequestParam(name = "status", required = false) @ValidCollectionStatusList String[] status,
        @RequestParam(name = FILTER_PARAMETER, required = false) @FilterLength String filter,
        @RequestParam(name = PAGE_PARAMETER, required = false) @ValidInteger String page,
        @RequestParam(name = SIZE_PARAMETER, required = false) @ValidInteger String size) {

        BaseDealer dealer = getLoggedDealer();

        InvoiceDateToFilterEnum dateToFilterEnum = InvoiceDateToFilterEnum.valueOfOrNull(dateToFilter);
        LocalDate startLocalDate = DateTimeHelper.parseFromIsoDate(startDate);
        LocalDate endLocalDate = DateTimeHelper.parseFromIsoDate(endDate);
        CollectionStatusEnum[] statusEnum = CollectionStatusEnum.valueOfArray(status);

        PageRequest pageRequest = createPageRequest(page, size);
        Page<BaseOrderCollection> collections = collectionService.find(dealer, startLocalDate, endLocalDate, dateToFilterEnum,
            statusEnum, filter, pageRequest);

        BaseResultCode resultCode = getQueryResultCode(collections.getContent());
        return new FindCollectionsRestResponse(resultCode, dtoMapper, collections);
    }

    @GetMapping("/report")
    @InfoLogging
    @ValidateParameters
    public FindCollectionsRestResponse find(
        @RequestParam(name = "startDate", required = false) @NotBlankParameter @ValidDate String startDate,
        @RequestParam(name = "endDate", required = false) @NotBlankParameter @ValidDate String endDate,
        @RequestParam(name = "dateToFilter", required = false) @ValidInvoiceDateToFilter String dateToFilter,
        @RequestParam(name = "status", required = false) @ValidCollectionStatusList String[] status,
        @RequestParam(name = FILTER_PARAMETER, required = false) @FilterLength String filter) {

        BaseDealer dealer = getLoggedDealer();

        InvoiceDateToFilterEnum dateToFilterEnum = InvoiceDateToFilterEnum.valueOfOrNull(dateToFilter);
        LocalDate startLocalDate = DateTimeHelper.parseFromIsoDate(startDate);
        LocalDate endLocalDate = DateTimeHelper.parseFromIsoDate(endDate);
        CollectionStatusEnum[] statusEnum = CollectionStatusEnum.valueOfArray(status);

        List<BaseOrderCollection> collections = collectionService.find(dealer, startLocalDate, endLocalDate, dateToFilterEnum,
            statusEnum, filter);

        BaseResultCode resultCode = getQueryResultCode(collections);
        return new FindCollectionsRestResponse(resultCode, dtoMapper, collections);
    }

    @GetMapping("/{token}")
    @InfoLogging
    public GetCollectionRestResponse get(@PathVariable String token) {
        BaseDealer dealer = getLoggedDealer();

        BaseOrderCollection collection = collectionService.getAndLoad(dealer, token);
        return new GetCollectionRestResponse(getSuccessResultCode(), dtoMapper, collection);
    }

    @PostMapping
    @InfoLogging
    @ValidateBody
    public AddCollectionRestResponse add(@RequestBody AddCollectionRestRequest request) {

        CollectionServiceRequest serviceRequest = dtoMapper.map(request, CollectionServiceRequest.class);
        BaseOrderCollection collection = collectionService.add(serviceRequest);

        return new AddCollectionRestResponse(getSuccessResultCode(), collection.getToken());
    }

    @PutMapping("/instalments")
    @InfoLogging
    @ValidateBody
    public GetCollectionInstalmentsRestResponse getInstalmments(@RequestBody GetCollectionInstalmentsRestRequest request) {
        BaseDealer dealer = getLoggedDealer();

        GetCollectionInstalmentsServiceRequest serviceRequest = dtoMapper.map(request,
            GetCollectionInstalmentsServiceRequest.class);

        List<OrderInstalmentServiceDTO> instalments = collectionService.getInstalments(dealer, serviceRequest);
        return new GetCollectionInstalmentsRestResponse(getSuccessResultCode(), dtoMapper, instalments);
    }

    @DeleteMapping("/{token}")
    @InfoLogging
    public RestResponseDTO cancel(@PathVariable String token) {
        BaseDealer dealer = getLoggedDealer();

        collectionService.cancel(dealer, token);
        return createSuccessResponse();
    }

}
