package org.example;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ShoppingCartApp {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        run(input, System.out, new LocalizationService(), new CartService());
        input.close();
    }

    static void run(Scanner input, PrintStream out,
                    LocalizationService localizationService,
                    CartService cartService) {

        out.println("Select language:");
        out.println("1. English");
        out.println("2. Finnish");
        out.println("3. Swedish");
        out.println("4. Japanese");
        out.println("5. Arabic");
        out.print("Enter choice: ");

        int choice = input.nextInt();
        String language = getLanguageFromChoice(choice);

        Map<String, String> strings = localizationService.getStrings(language);

        out.print(strings.get("itemCount") + ": ");
        int itemCount = input.nextInt();

        double cartTotal = 0.0;
        List<CartItem> items = new ArrayList<>();

        for (int i = 1; i <= itemCount; i++) {
            out.print(strings.get("price") + " " + i + ": ");
            double price = input.nextDouble();

            out.print(strings.get("quantity") + " " + i + ": ");
            int quantity = input.nextInt();

            double subtotal = ShoppingCartCalculator.calculateItemTotal(price, quantity);
            cartTotal += subtotal;

            items.add(new CartItem(i, price, quantity, subtotal));
        }

        out.println(strings.get("cartTotal") + ": " + cartTotal);

        cartService.saveCart(itemCount, cartTotal, language, items);
    }

    static String getLanguageFromChoice(int choice) {
        switch (choice) {
            case 2:
                return "fi";
            case 3:
                return "sv";
            case 4:
                return "ja";
            case 5:
                return "ar";
            default:
                return "en";
        }
    }
}