package ua.rd.pizzaservice.repository;

import ua.rd.pizzaservice.domain.Pizza;

/**
 * Created by Oleh_Kakherskyi on 10/4/2016.
 */
public interface PizzaRepository {

    Pizza find(int id);
}
