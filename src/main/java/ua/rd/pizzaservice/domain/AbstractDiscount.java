package ua.rd.pizzaservice.domain;

/**
 * Created by Oleg on 10.10.2016.
 */
public abstract class AbstractDiscount {

    public void applyDiscount(Order order) {
        if (canBeApplied(order)) {
            order.addDiscount(this);
        }
    }

    public abstract double calculateDiscount(Order order);

    protected abstract boolean canBeApplied(Order order);

}
