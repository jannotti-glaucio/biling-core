package tech.jannotti.billing.core.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.persistence.model.base.BaseCity;
import tech.jannotti.billing.core.persistence.model.base.BaseCountry;
import tech.jannotti.billing.core.persistence.model.base.BaseState;
import tech.jannotti.billing.core.persistence.repository.base.CityRepository;
import tech.jannotti.billing.core.persistence.repository.base.CountryRepository;
import tech.jannotti.billing.core.persistence.repository.base.StateRepository;

@Service
public class AddressService extends AbstractService {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CityRepository cityRepository;

    public BaseCountry getCountry(String code) {
        return countryRepository.getByCode(code);
    }

    public List<BaseState> listStates(BaseCountry country) {
        return stateRepository.findByCountry(country);
    }

    public BaseState getState(BaseCountry country, String code) {
        return stateRepository.getByCountryAndCode(country, code);
    }

    public List<BaseCity> listCities(BaseState state) {
        return cityRepository.findByState(state);
    }

    public BaseCity getCity(String countryCode, String stateCode, String name) {

        BaseCountry country = countryRepository.getByCode(countryCode);
        BaseState state = stateRepository.getByCountryAndCode(country, stateCode);

        BaseCity city = cityRepository.getByStateAndNameIgnoreCase(state, name);
        return city;
    }

}