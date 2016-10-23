package ua.rd.pizzaservice.domain;

/**
 * Created by Oleh_Kakherskyi on 10/12/2016.
 */
public class CustomerCardDiscount extends AbstractDiscount {

    private double maxPercentageFromOrderPrice;

    private double customerCardPercentageDiscount;

    public CustomerCardDiscount(double maxPercentageFromOrderPrice, double customerCardPercentageDiscount) {
        this.maxPercentageFromOrderPrice = maxPercentageFromOrderPrice;
        this.customerCardPercentageDiscount = customerCardPercentageDiscount;
    }

    @Override
    public double calculateDiscount(Order order) {
        double customerCardDiscount = getDiscountFromCustomerCard(order);
        double maxDiscount = getMaxPossibleDiscount(order);
        return (maxDiscount < customerCardDiscount) ? maxDiscount : customerCardDiscount;
    }

    @Override
    protected boolean canBeApplied(Order order) {
        return order.getCustomer().getCustomerCard() != null;
    }

    private double getDiscountFromCustomerCard(Order order) {
        return order.getCustomer().getCustomerCard().getCardTotalPrice() * customerCardPercentageDiscount;
    }

    private double getMaxPossibleDiscount(Order order) {
        return order.calculateOrderPrice() * maxPercentageFromOrderPrice;
    }
}
