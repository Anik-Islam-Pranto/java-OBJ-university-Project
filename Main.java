import gui.ShoppingCartGUI;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new ShoppingCartGUI().setVisible(true);
        });
    }
}