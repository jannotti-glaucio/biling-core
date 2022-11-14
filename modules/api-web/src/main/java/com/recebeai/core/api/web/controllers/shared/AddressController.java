package tech.jannotti.billing.core.api.web.controllers.shared;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.api.web.controllers.AbstractWebController;
import tech.jannotti.billing.core.api.web.controllers.dto.response.address.ListCitiesRestResponse;
import tech.jannotti.billing.core.api.web.controllers.dto.response.address.ListStatesRestResponse;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.persistence.model.base.BaseCity;
import tech.jannotti.billing.core.persistence.model.base.BaseCountry;
import tech.jannotti.billing.core.persistence.model.base.BaseState;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.exception.ResultCodeControllerException;
import tech.jannotti.billing.core.services.AddressService;
import tech.jannotti.billing.core.validation.extension.annotations.model.ValidCountry;

@RestController
@RequestMapping(ApiConstants.V1_API_PATH + "address")
public class AddressController extends AbstractWebController {

    @Autowired
    private AddressService addressService;

    @GetMapping(value = "/state/{country}")
    @InfoLogging
    public ListStatesRestResponse listStates(
        @PathVariable("country") @ValidCountry String countryCode) {

        BaseCountry country = addressService.getCountry(countryCode);

        List<BaseState> states = addressService.listStates(country);

        BaseResultCode resultCode = getQueryResultCode(states);
        return new ListStatesRestResponse(resultCode, dtoMapper, states);
    }

    @GetMapping(value = "/city/{country}/{state}")
    @InfoLogging
    public ListCitiesRestResponse listCities(
        @PathVariable("country") @ValidCountry String countryCode,
        @PathVariable("state") String stateCode) {

        BaseCountry country = addressService.getCountry(countryCode);

        BaseState state = addressService.getState(country, stateCode);
        if (state == null)
            throw new ResultCodeControllerException(CODE_INVALID_STATE_VALUE, stateCode);

        List<BaseCity> cities = addressService.listCities(state);

        BaseResultCode resultCode = getQueryResultCode(cities);
        return new ListCitiesRestResponse(resultCode, dtoMapper, cities);
    }

}
