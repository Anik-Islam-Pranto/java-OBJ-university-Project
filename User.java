package model;

public class User {
    private String username;
    private Cart cart;
    
    public User(String username) {
        this.username = username;
        this.cart = new Cart();
    }
    
    public String getUsername() {
        return username;
    }
    
    public Cart getCart() {
        return cart;
    }
}