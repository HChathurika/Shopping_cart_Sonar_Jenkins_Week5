package org.example;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ShoppingCartAppTest {

    @Test
    void getLanguageFromChoiceShouldReturnEnglishByDefault() {
        assertEquals("en", ShoppingCartApp.getLanguageFromChoice(1));
        assertEquals("en", ShoppingCartApp.getLanguageFromChoice(99));
    }

    @Test
    void getLanguageFromChoiceShouldReturnFinnish() {
        assertEquals("fi", ShoppingCartApp.getLanguageFromChoice(2));
    }

    @Test
    void getLanguageFromChoiceShouldReturnSwedish() {
        assertEquals("sv", ShoppingCartApp.getLanguageFromChoice(3));
    }

    @Test
    void getLanguageFromChoiceShouldReturnJapanese() {
        assertEquals("ja", ShoppingCartApp.getLanguageFromChoice(4));
    }

    @Test
    void getLanguageFromChoiceShouldReturnArabic() {
        assertEquals("ar", ShoppingCartApp.getLanguageFromChoice(5));
    }

    @Test
    void runShouldCalculateTotalAndSaveCartForEnglish() {
        String simulatedInput = "1\n2\n100.0\n2\n50.0\n3\n";
        Scanner scanner = new Scanner(simulatedInput);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        LocalizationService localizationService = mock(LocalizationService.class);
        CartService cartService = mock(CartService.class);

        Map<String, String> strings = new HashMap<>();
        strings.put("itemCount", "Item count");
        strings.put("price", "Price");
        strings.put("quantity", "Quantity");
        strings.put("cartTotal", "Cart total");

        when(localizationService.getStrings("en")).thenReturn(strings);

        ShoppingCartApp.run(scanner, out, localizationService, cartService);

        verify(localizationService).getStrings("en");
        verify(cartService).saveCart(eq(2), eq(350.0), eq("en"), anyList());
    }

    @Test
    void runShouldCalculateTotalAndSaveCartForArabic() {
        String simulatedInput = "5\n1\n20.0\n2\n";
        Scanner scanner = new Scanner(simulatedInput);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        LocalizationService localizationService = mock(LocalizationService.class);
        CartService cartService = mock(CartService.class);

        Map<String, String> strings = new HashMap<>();
        strings.put("itemCount", "عدد العناصر");
        strings.put("price", "السعر");
        strings.put("quantity", "الكمية");
        strings.put("cartTotal", "المجموع");

        when(localizationService.getStrings("ar")).thenReturn(strings);

        ShoppingCartApp.run(scanner, out, localizationService, cartService);

        verify(localizationService).getStrings("ar");
        verify(cartService).saveCart(eq(1), eq(40.0), eq("ar"), anyList());
    }

    @Test
    void runShouldUseFinnishLanguage() {
        String simulatedInput = "2\n1\n10.0\n2\n";
        Scanner scanner = new Scanner(simulatedInput);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        LocalizationService localizationService = mock(LocalizationService.class);
        CartService cartService = mock(CartService.class);

        Map<String, String> strings = new HashMap<>();
        strings.put("itemCount", "Tuotteet");
        strings.put("price", "Hinta");
        strings.put("quantity", "Määrä");
        strings.put("cartTotal", "Yhteensä");

        when(localizationService.getStrings("fi")).thenReturn(strings);

        ShoppingCartApp.run(scanner, out, localizationService, cartService);

        verify(localizationService).getStrings("fi");
        verify(cartService).saveCart(eq(1), eq(20.0), eq("fi"), anyList());
    }

    @Test
    void runShouldUseSwedishLanguage() {
        String simulatedInput = "3\n1\n15.0\n2\n";
        Scanner scanner = new Scanner(simulatedInput);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        LocalizationService localizationService = mock(LocalizationService.class);
        CartService cartService = mock(CartService.class);

        Map<String, String> strings = new HashMap<>();
        strings.put("itemCount", "Antal");
        strings.put("price", "Pris");
        strings.put("quantity", "Antal");
        strings.put("cartTotal", "Totalt");

        when(localizationService.getStrings("sv")).thenReturn(strings);

        ShoppingCartApp.run(scanner, out, localizationService, cartService);

        verify(localizationService).getStrings("sv");
        verify(cartService).saveCart(eq(1), eq(30.0), eq("sv"), anyList());
    }

    @Test
    void runShouldUseJapaneseLanguage() {
        String simulatedInput = "4\n1\n12.5\n4\n";
        Scanner scanner = new Scanner(simulatedInput);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        LocalizationService localizationService = mock(LocalizationService.class);
        CartService cartService = mock(CartService.class);

        Map<String, String> strings = new HashMap<>();
        strings.put("itemCount", "商品数");
        strings.put("price", "価格");
        strings.put("quantity", "数量");
        strings.put("cartTotal", "合計");

        when(localizationService.getStrings("ja")).thenReturn(strings);

        ShoppingCartApp.run(scanner, out, localizationService, cartService);

        verify(localizationService).getStrings("ja");
        verify(cartService).saveCart(eq(1), eq(50.0), eq("ja"), anyList());
    }

    @Test
    void runShouldHandleZeroItems() {
        String simulatedInput = "1\n0\n";
        Scanner scanner = new Scanner(simulatedInput);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        LocalizationService localizationService = mock(LocalizationService.class);
        CartService cartService = mock(CartService.class);

        Map<String, String> strings = new HashMap<>();
        strings.put("itemCount", "Item count");
        strings.put("price", "Price");
        strings.put("quantity", "Quantity");
        strings.put("cartTotal", "Cart total");

        when(localizationService.getStrings("en")).thenReturn(strings);

        ShoppingCartApp.run(scanner, out, localizationService, cartService);

        verify(localizationService).getStrings("en");
        verify(cartService).saveCart(eq(0), eq(0.0), eq("en"), anyList());
    }

    @Test
    void runShouldPrintMenuAndCartTotalText() {
        String simulatedInput = "1\n1\n10.0\n2\n";
        Scanner scanner = new Scanner(simulatedInput);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        LocalizationService localizationService = mock(LocalizationService.class);
        CartService cartService = mock(CartService.class);

        Map<String, String> strings = new HashMap<>();
        strings.put("itemCount", "Item count");
        strings.put("price", "Price");
        strings.put("quantity", "Quantity");
        strings.put("cartTotal", "Cart total");

        when(localizationService.getStrings("en")).thenReturn(strings);

        ShoppingCartApp.run(scanner, out, localizationService, cartService);

        String output = outputStream.toString();
        assertTrue(output.contains("Select language:"));
        assertTrue(output.contains("Enter choice:"));
        assertTrue(output.contains("Cart total: 20.0"));
    }

    @Test
    void runShouldPassCorrectCartItemsToSaveCart() {
        String simulatedInput = "1\n2\n10.0\n2\n5.0\n4\n";
        Scanner scanner = new Scanner(simulatedInput);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        LocalizationService localizationService = mock(LocalizationService.class);
        CartService cartService = mock(CartService.class);

        Map<String, String> strings = new HashMap<>();
        strings.put("itemCount", "Item count");
        strings.put("price", "Price");
        strings.put("quantity", "Quantity");
        strings.put("cartTotal", "Cart total");

        when(localizationService.getStrings("en")).thenReturn(strings);

        ShoppingCartApp.run(scanner, out, localizationService, cartService);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<CartItem>> itemsCaptor = ArgumentCaptor.forClass(List.class);

        verify(cartService).saveCart(eq(2), eq(40.0), eq("en"), itemsCaptor.capture());

        List<CartItem> items = itemsCaptor.getValue();
        assertEquals(2, items.size());

        assertEquals(1, items.get(0).getItemNumber());
        assertEquals(10.0, items.get(0).getPrice());
        assertEquals(2, items.get(0).getQuantity());
        assertEquals(20.0, items.get(0).getSubtotal());

        assertEquals(2, items.get(1).getItemNumber());
        assertEquals(5.0, items.get(1).getPrice());
        assertEquals(4, items.get(1).getQuantity());
        assertEquals(20.0, items.get(1).getSubtotal());
    }

    @Test
    void runShouldPrintPromptsForEachItem() {
        String simulatedInput = "1\n2\n10.0\n1\n20.0\n2\n";
        Scanner scanner = new Scanner(simulatedInput);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        LocalizationService localizationService = mock(LocalizationService.class);
        CartService cartService = mock(CartService.class);

        Map<String, String> strings = new HashMap<>();
        strings.put("itemCount", "Item count");
        strings.put("price", "Price");
        strings.put("quantity", "Quantity");
        strings.put("cartTotal", "Cart total");

        when(localizationService.getStrings("en")).thenReturn(strings);

        ShoppingCartApp.run(scanner, out, localizationService, cartService);

        String output = outputStream.toString();
        assertTrue(output.contains("Price 1:"));
        assertTrue(output.contains("Quantity 1:"));
        assertTrue(output.contains("Price 2:"));
        assertTrue(output.contains("Quantity 2:"));
    }
}