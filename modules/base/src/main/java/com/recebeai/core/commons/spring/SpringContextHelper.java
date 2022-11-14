package tech.jannotti.billing.core.commons.spring;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHelper {

    @Autowired
    Environment environment;

    public boolean isOnTestsProfile() {
        return Arrays.asList(environment.getActiveProfiles()).contains(SpringConstants.TESTS_PROFILE);
    }

}
