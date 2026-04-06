package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ShoppingCartApp {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Select language:");
        System.out.println("1. English");
        System.out.println("2. Finnish");
        System.out.println("3. Swedish");
        System.out.println("4. Japanese");
        System.out.println("5. Arabic");
        System.out.print("Enter choice: ");

        int choice = input.nextInt();
        String language;

        switch (choice) {
            case 2:
                language = "fi";
                break;
            case 3:
                language = "sv";
                break;
            case 4:
                language = "ja";
                break;
            case 5:
                language = "ar";
                break;
            default:
                language = "en";
                break;
        }

        LocalizationService localizationService = new LocalizationService();
        Map<String, String> strings = localizationService.getStrings(language);

        System.out.print(strings.get("itemCount") + ": ");
        int itemCount = input.nextInt();

        double cartTotal = 0.0;
        List<CartItem> items = new ArrayList<>();

        for (int i = 1; i <= itemCount; i++) {
            System.out.print(strings.get("price") + " " + i + ": ");
            double price = input.nextDouble();

            System.out.print(strings.get("quantity") + " " + i + ": ");
            int quantity = input.nextInt();

            double subtotal = ShoppingCartCalculator.calculateItemTotal(price, quantity);
            cartTotal += subtotal;

            items.add(new CartItem(i, price, quantity, subtotal));
        }

        System.out.println(strings.get("cartTotal") + ": " + cartTotal);

        CartService cartService = new CartService();
        cartService.saveCart(itemCount, cartTotal, language, items);

        input.close();
    }
}