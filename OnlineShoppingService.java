package service;

import discount.FixedDiscount;
import discount.PercentageDiscount;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import model.Product;
import model.User; 

public class OnlineShoppingService {
    private List<Product> availableProducts = new ArrayList<>();
    
    public OnlineShoppingService() {
        initializeProducts();
    }
    
    private void initializeProducts() {
        availableProducts.add(new Product("Laptop", 999.99, new PercentageDiscount(10)));
        availableProducts.add(new Product("Smartphone", 599.99, new FixedDiscount(50)));
        availableProducts.add(new Product("Headphones", 99.99));
        availableProducts.add(new Product("Tablet", 299.99, new PercentageDiscount(15)));
        availableProducts.add(new Product("Mouse", 24.99, new FixedDiscount(5)));
    }
    
    public List<Product> getAvailableProducts() {
        return availableProducts;
    }
    
    public void addToCart(User user, int productIndex, int quantity) {
        Product product = availableProducts.get(productIndex);
        user.getCart().addItem(product, quantity);
    }
    
    public void removeFromCart(User user, int itemIndex, int quantity) {
        CartItem item = user.getCart().getItems().get(itemIndex);
        user.getCart().removeItem(item.getProduct().getName(), quantity);
    }
    
    public List<String> getCartItems(User user) {
        List<String> items = new ArrayList<>();
        for (CartItem item : user.getCart().getItems()) {
            items.add(item.toString());
        }
        return items;
    }
    
    public double getCartTotal(User user) {
        return user.getCart().getTotal();
    }
    
    public double checkout(User user) {
        double total = user.getCart().getTotal();
        if (total > 0) {
            user.getCart().clearCart();
        }
        return total;
    }
    
    public void clearCart(User user) {
        user.getCart().clearCart();
    }
}