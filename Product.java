package model;

import discount.Discount;
import discount.NoDiscount;

public class Product {
    private String name;
    private double price;
    private Discount discount;
    
    public Product(String name, double price) {
        this(name, price, new NoDiscount());
    }
    
    public Product(String name, double price, Discount discount) {
        this.name = name;
        this.price = price;
        this.discount = discount;
    }
    
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return discount.applyDiscount(price);
    }
    
    public double getOriginalPrice() {
        return price;
    }
    
    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
    
    @Override
    public String toString() {
        return String.format("%s - $%.2f%s", name, getPrice(), 
            (discount instanceof NoDiscount) ? "" : " (discounted from $" + getOriginalPrice() + ")");
    }
}