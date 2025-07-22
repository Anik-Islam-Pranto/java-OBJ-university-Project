package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import model.Product;
import model.User;
import service.OnlineShoppingService;

public class ShoppingCartGUI extends JFrame {
    private OnlineShoppingService shoppingService;
    private User currentUser;
    private DefaultListModel<String> productListModel;
    private DefaultListModel<String> cartListModel;
    
    public ShoppingCartGUI() {
        shoppingService = new OnlineShoppingService();
        currentUser = new User("GUI User");
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Online Shopping Cart");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Welcome label at top
        JLabel welcomeLabel = new JLabel("Welcome to Online Shopping Cart", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);
        
        // Center panel with products and cart
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // Products panel
        JPanel productsPanel = new JPanel(new BorderLayout());
        productsPanel.setBorder(BorderFactory.createTitledBorder("Available Products"));
        
        productListModel = new DefaultListModel<>();
        updateProductList();
        
        JList<String> productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane productScrollPane = new JScrollPane(productList);
        productsPanel.add(productScrollPane, BorderLayout.CENTER);
        
        // Add to cart button
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(e -> {
            int selectedIndex = productList.getSelectedIndex();
            if (selectedIndex != -1) {
                String input = JOptionPane.showInputDialog(this, "Enter quantity:", "Add to Cart", JOptionPane.PLAIN_MESSAGE);
                try {
                    int quantity = Integer.parseInt(input);
                    if (quantity > 0) {
                        shoppingService.addToCart(currentUser, selectedIndex, quantity);
                        updateCartList();
                        JOptionPane.showMessageDialog(this, "Product added to cart!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Quantity must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a product first.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        productsPanel.add(addToCartButton, BorderLayout.SOUTH);
        
        // Cart panel
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Your Shopping Cart"));
        
        cartListModel = new DefaultListModel<>();
        JList<String> cartList = new JList<>(cartListModel);
        JScrollPane cartScrollPane = new JScrollPane(cartList);
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);
        
        // Cart buttons panel
        JPanel cartButtonsPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            int selectedIndex = cartList.getSelectedIndex();
            if (selectedIndex != -1) {
                String input = JOptionPane.showInputDialog(this, "Enter quantity to remove:", "Remove from Cart", JOptionPane.PLAIN_MESSAGE);
                try {
                    int quantity = Integer.parseInt(input);
                    if (quantity > 0) {
                        shoppingService.removeFromCart(currentUser, selectedIndex, quantity);
                        updateCartList();
                    } else {
                        JOptionPane.showMessageDialog(this, "Quantity must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item first.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cartButtonsPanel.add(removeButton);
        
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> {
            double total = shoppingService.checkout(currentUser);
            if (total > 0) {
                JOptionPane.showMessageDialog(this, 
                    String.format("Thank you for your purchase!\nTotal: $%.2f", total), 
                    "Order Complete", JOptionPane.INFORMATION_MESSAGE);
                updateCartList();
            } else {
                JOptionPane.showMessageDialog(this, "Your cart is empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cartButtonsPanel.add(checkoutButton);
        
        JButton clearButton = new JButton("Clear Cart");
        clearButton.addActionListener(e -> {
            shoppingService.clearCart(currentUser);
            updateCartList();
        });
        cartButtonsPanel.add(clearButton);
        
        cartPanel.add(cartButtonsPanel, BorderLayout.SOUTH);
        
        centerPanel.add(productsPanel);
        centerPanel.add(cartPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Total label at bottom
        JLabel totalLabel = new JLabel("Total: $0.00", JLabel.RIGHT);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(totalLabel, BorderLayout.SOUTH);
        
        // Add action listener to update total when cart changes
        cartListModel.addListDataListener(new javax.swing.event.ListDataListener() {
            public void intervalAdded(javax.swing.event.ListDataEvent e) { updateTotal(); }
            public void intervalRemoved(javax.swing.event.ListDataEvent e) { updateTotal(); }
            public void contentsChanged(javax.swing.event.ListDataEvent e) { updateTotal(); }
            
            private void updateTotal() {
                double total = shoppingService.getCartTotal(currentUser);
                totalLabel.setText(String.format("Total: $%.2f", total));
            }
        });
        
        add(mainPanel);
    }
    
    private void updateProductList() {
        productListModel.clear();
        List<Product> products = shoppingService.getAvailableProducts();
        for (Product product : products) {
            productListModel.addElement(product.toString());
        }
    }
    
    private void updateCartList() {
        cartListModel.clear();
        List<String> cartItems = shoppingService.getCartItems(currentUser);
        for (String item : cartItems) {
            cartListModel.addElement(item);
        }
    }
}