package discount;

public class FixedDiscount implements Discount {
    private double amount;
    
    public FixedDiscount(double amount) {
        this.amount = amount;
    }
    
    @Override
    public double applyDiscount(double originalPrice) {
        return Math.max(0, originalPrice - amount);
    }
}