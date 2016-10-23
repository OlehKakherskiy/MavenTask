package ua.rd.pizzaservice.domain;

/**
 * Created by Oleh_Kakherskyi on 10/4/2016.
 */
public class Customer {

    private Long id;

    private String name;

    private String address;

    private CustomerCard customerCard;

    public Customer(Long id, String name, String address, CustomerCard customerCard) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.customerCard = customerCard;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CustomerCard getCustomerCard() {
        return customerCard;
    }

    public void setCustomerCard(CustomerCard customerCard) {
        this.customerCard = customerCard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (id != null ? !id.equals(customer.id) : customer.id != null) return false;
        if (!name.equals(customer.name)) return false;
        if (!address.equals(customer.address)) return false;
        return customerCard != null ? customerCard.equals(customer.customerCard) : customer.customerCard == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + (customerCard != null ? customerCard.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", customerCard=" + customerCard +
                '}';
    }
}
