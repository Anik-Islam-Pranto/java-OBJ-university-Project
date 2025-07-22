package discount;

public class NoDiscount implements Discount {
    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice;
    }
}