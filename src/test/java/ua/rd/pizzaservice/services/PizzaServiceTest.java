package ua.rd.pizzaservice.services;

import org.junit.Assert;
import org.junit.Test;
import ua.rd.pizzaservice.domain.Pizza;
import ua.rd.pizzaservice.repository.PizzaRepository;
import ua.rd.pizzaservice.domain.Pizza.PizzaType;

import static org.mockito.Mockito.*;

/**
 * Created by Oleh_Kakherskyi on 10/10/2016.
 */
public class PizzaServiceTest {

    @Test
    public void find() throws Exception {
        PizzaRepository pizzaRepository = mock(PizzaRepository.class);
        when(pizzaRepository.find(1)).thenReturn(new Pizza(1, "name", 1.0, PizzaType.MEAT));

        PizzaService pizzaService = new SimplePizzaService(pizzaRepository);

        Assert.assertEquals(new Pizza(1, "name", 1.0, PizzaType.MEAT), pizzaService.find(1));
        verify(pizzaRepository).find(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findNotExistedPizza() {
        PizzaRepository pizzaRepository = mock(PizzaRepository.class);
        when(pizzaRepository.find(500)).thenThrow(IllegalArgumentException.class);

        PizzaService pizzaService = new SimplePizzaService(pizzaRepository);
        pizzaService.find(500);
    }
}