package ua.rd.pizzaservice.domain;

/**
 * Created by Oleh_Kakherskyi on 10/12/2016.
 */
public class MaxPizzaPriceDiscount extends AbstractDiscount {

    private int pizzaCount;

    private double percentageAmount;

    public MaxPizzaPriceDiscount(int pizzaCount, double percentageAmount) {
        this.pizzaCount = pizzaCount;
        this.percentageAmount = percentageAmount;
    }

    @Override
    protected boolean canBeApplied(Order order) {
        return order.getPizzaCount() >= pizzaCount;
    }

    @Override
    public double calculateDiscount(Order order) {
        return order.findMaxPizzaPrice() * percentageAmount;
    }
}
