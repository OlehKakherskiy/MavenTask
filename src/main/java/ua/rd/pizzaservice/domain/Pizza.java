package ua.rd.pizzaservice.domain;

/**
 * Created by Oleh_Kakherskyi on 10/4/2016.
 */
public class Pizza {

    private Integer id;

    private String name;

    private Double price;

    private PizzaType pizzaType;

    public enum PizzaType{
        VEGETARIAN,

        SEA,

        MEAT
    }

    public Pizza(Integer id, String name, Double price, PizzaType pizzaType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.pizzaType = pizzaType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public PizzaType getPizzaType() {
        return pizzaType;
    }

    public void setPizzaType(PizzaType pizzaType) {
        this.pizzaType = pizzaType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pizza pizza = (Pizza) o;

        if (!id.equals(pizza.id)) return false;
        if (!name.equals(pizza.name)) return false;
        if (!price.equals(pizza.price)) return false;
        return pizzaType == pizza.pizzaType;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + pizzaType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", pizzaType=" + pizzaType +
                '}';
    }
}
