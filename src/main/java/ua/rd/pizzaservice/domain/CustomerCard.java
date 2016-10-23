package ua.rd.pizzaservice.domain;

/**
 * Created by Oleh_Kakherskyi on 10/12/2016.
 */
public class CustomerCard {

    private Long id;

    private double totalPrice;

    public CustomerCard(Long id, double totalPrice) {
        this.id = id;
        this.totalPrice = totalPrice;
    }

    public void addOrderPrice(double price) {
        totalPrice += price;
    }

    public double getCardTotalPrice() {
        return totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerCard that = (CustomerCard) o;

        if (Double.compare(that.totalPrice, totalPrice) != 0) return false;
        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        temp = Double.doubleToLongBits(totalPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
