package tech.jannotti.billing.core.commons.bean;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NormalizerHelperTest {

    @Test
    public void normalize_success() {
        String value = NormalizerHelper.normalize("AVENIDA RIO BRANCO, 611, 5° ANDAR, ");
        assertEquals("AVENIDA RIO BRANCO, 611, 5 ANDAR", value);
    }

}
