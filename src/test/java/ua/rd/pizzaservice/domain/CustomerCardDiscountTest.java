package ua.rd.pizzaservice.domain;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

/**
 * Created by Oleh_Kakherskyi on 10/12/2016.
 */
public class CustomerCardDiscountTest {

    public static final double DELTA = 0.000000000001;
    private static AbstractDiscount customerCardDiscount;

    @BeforeClass
    public static void beforeClass() {
        customerCardDiscount = new CustomerCardDiscount(0.3, 0.1);
    }

    @Test
    public void calculateDiscount() throws Exception {
        Order order = mock(Order.class);
        when(order.getCustomer()).thenReturn(new Customer(1L, "n", "s", new CustomerCard(1L, 100.0)));
        when(order.calculateOrderPrice()).thenReturn(150.0);
        Assert.assertEquals(10.0, customerCardDiscount.calculateDiscount(order), DELTA);
        verify(order).getCustomer();
        verify(order).calculateOrderPrice();
    }

    @Test
    public void calculateDiscountCheckMaxDiscount() throws Exception {
        Order order = mock(Order.class);
        when(order.getCustomer()).thenReturn(new Customer(1L, "n", "s", new CustomerCard(1L, 15000.0)));
        when(order.calculateOrderPrice()).thenReturn(100.0);
        Assert.assertEquals(30.0, customerCardDiscount.calculateDiscount(order), DELTA);
        verify(order).getCustomer();
        verify(order).calculateOrderPrice();
    }

    @Test
    public void canBeApplied_UserCardExists() throws Exception {
        Order order = new Order(getCustomerMock(new CustomerCard(1L, 0.0)), Collections.emptyMap(), Collections.emptyList());
        Assert.assertTrue(customerCardDiscount.canBeApplied(order));
    }

    @Test
    public void canBeApplied_UserCardNotExists() throws Exception {
        Order order = new Order(getCustomerMock(null), Collections.emptyMap(), Collections.emptyList());
        Assert.assertFalse(customerCardDiscount.canBeApplied(order));
    }

    private Customer getCustomerMock(CustomerCard customerCard) {
        Customer customer = mock(Customer.class);
        when(customer.getCustomerCard()).thenReturn(customerCard);
        return customer;
    }

}