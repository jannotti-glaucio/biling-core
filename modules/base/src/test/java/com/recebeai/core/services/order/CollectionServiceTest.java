package tech.jannotti.billing.core.services.order;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CollectionServiceTest {

    @Test
    public void calculateAmount_singleInstalment() {
        CollectionService service = new CollectionService();

        int amount = service.calculateAmount(100, 1, 1);
        assertEquals(100, amount);
    }

    @Test
    public void calculateAmount_multipleInstalments() {
        CollectionService service = new CollectionService();

        int amount1 = service.calculateAmount(100, 1, 2);
        assertEquals(50, amount1);

        int amount2 = service.calculateAmount(100, 2, 2);
        assertEquals(50, amount2);
    }

    @Test
    public void calculateAmount_multipleInstalmentsWithRemainder() {
        CollectionService service = new CollectionService();

        int amount1 = service.calculateAmount(100, 1, 3);
        assertEquals(34, amount1);

        int amount2 = service.calculateAmount(100, 2, 3);
        assertEquals(33, amount2);

        int amount3 = service.calculateAmount(100, 3, 3);
        assertEquals(33, amount3);
    }

}
