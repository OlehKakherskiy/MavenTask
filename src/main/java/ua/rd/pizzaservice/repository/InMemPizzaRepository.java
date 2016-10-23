package ua.rd.pizzaservice.repository;

import org.springframework.stereotype.Repository;
import ua.rd.pizzaservice.domain.Pizza;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import ua.rd.pizzaservice.domain.Pizza.PizzaType;
import ua.rd.pizzaservice.infrastructure.Benchmark;

/**
 * Created by Oleh_Kakherskyi on 10/4/2016.
 */
@Repository
public class InMemPizzaRepository implements PizzaRepository {

    private Map<Integer, Pizza> pizzas;

    @PostConstruct
    public void init() {
        pizzas = new HashMap<Integer, Pizza>() {{
            put(1, new Pizza(1, "meatPizza", 100.0, Pizza.PizzaType.MEAT));
            put(2, new Pizza(2, "seaPizza", 150.0, PizzaType.SEA));
            put(3, new Pizza(3, "veganPizza", 124.0, PizzaType.VEGETARIAN));
        }};
    }


    @Benchmark(switchedOn = true)
    @Override
    public Pizza find(int id) {
        Pizza result = pizzas.get(id);
        if (result == null) {
            throw new IllegalArgumentException("There's no pizza with id = " + id);
        }
        return pizzas.get(id);
    }
}
