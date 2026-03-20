import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ShoppingCartApp {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Select language / Valitse kieli / Välj språk:");
        System.out.println("1. English");
        System.out.println("2. Finnish");
        System.out.println("3. Swedish");
        System.out.print("Enter choice: ");

        int choice = input.nextInt();
        Locale locale;

        switch (choice) {
            case 2:
                locale = new Locale("fi", "FI");
                break;
            case 3:
                locale = new Locale("sv", "SE");
                break;
            case 1:
            default:
                locale = new Locale("en", "US");
                break;
        }

        ResourceBundle rb = ResourceBundle.getBundle("MessagesBundle", locale);

        System.out.print(rb.getString("itemCount"));
        int itemCount = input.nextInt();

        double cartTotal = 0.0;

        for (int i = 1; i <= itemCount; i++) {
            System.out.println(rb.getString("item") + " " + i);

            System.out.print(rb.getString("price"));
            double price = input.nextDouble();

            System.out.print(rb.getString("quantity"));
            int quantity = input.nextInt();

            double itemTotal = calculateItemTotal(price, quantity);
            cartTotal += itemTotal;

            System.out.println(rb.getString("itemTotal") + " " + itemTotal);
        }

        System.out.println(rb.getString("cartTotal") + " " + cartTotal);
        input.close();
    }

    public static double calculateItemTotal(double price, int quantity) {
        return price * quantity;
    }

    public static double calculateCartTotal(double[] prices, int[] quantities) {
        double total = 0.0;
        for (int i = 0; i < prices.length; i++) {
            total += calculateItemTotal(prices[i], quantities[i]);
        }
        return total;
    }
}