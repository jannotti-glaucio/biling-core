package tech.jannotti.billing.core.commons.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;

public abstract class AbstractBeanMapper {

    protected ModelMapper modelMapper;

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void init() {
        modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
            .setFieldMatchingEnabled(true)
            .setMatchingStrategy(MatchingStrategies.STANDARD);

        configure();
    }

    public <T> T map(Object source, Class<T> destinationType) {
        return modelMapper.map(source, destinationType);
    }

    public void map(Object source, Object destinaton) {
        modelMapper.map(source, destinaton);
    }

    public <T> List<T> mapList(List<?> sources, Class<T> destinationType) {
        List<T> destinations = new ArrayList<T>();

        if (CollectionUtils.isNotEmpty(sources)) {
            for (Object source : sources) {
                T destintation = map(source, destinationType);
                destinations.add(destintation);
            }
        }
        return destinations;
    }

    public <T> List<T> mapList(Page<?> page, Class<T> destinationType) {
        return mapList(page.getContent(), destinationType);
    }

    protected abstract void configure();

    protected <T extends Converter<S, D>, S, D> Converter<S, D> getManagedConverter(Class<T> converterClass) {
        Converter<S, D> converter = context.getBean(converterClass);
        return converter;
    }

}