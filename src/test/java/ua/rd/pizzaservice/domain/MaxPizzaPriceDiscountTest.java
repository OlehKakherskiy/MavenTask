package ua.rd.pizzaservice.domain;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by Oleh_Kakherskyi on 10/12/2016.
 */
public class MaxPizzaPriceDiscountTest {

    private static AbstractDiscount maxPizzaDiscount;

    @BeforeClass
    public static void beforeClass() throws Exception {
        maxPizzaDiscount = new MaxPizzaPriceDiscount(5, 0.3);
    }

    @Test
    public void testCanBeApplied_enoughPizzaCount() throws Exception {
        Order order = mock(Order.class);
        when(order.getPizzaCount()).thenReturn(5);
        Assert.assertTrue(maxPizzaDiscount.canBeApplied(order));
        verify(order).getPizzaCount();
    }

    @Test
    public void testCanBeApplied_notEnoughPizzaCount() throws Exception {
        Order order = mock(Order.class);
        when(order.getPizzaCount()).thenReturn(4);
        Assert.assertFalse(maxPizzaDiscount.canBeApplied(order));
    }

    @Test
    public void calculateDiscount() throws Exception {
        Order order = mock(Order.class);
        when(order.findMaxPizzaPrice()).thenReturn(125.0);
        Assert.assertEquals(37.5, maxPizzaDiscount.calculateDiscount(order), 0.0000000000001);
    }

}