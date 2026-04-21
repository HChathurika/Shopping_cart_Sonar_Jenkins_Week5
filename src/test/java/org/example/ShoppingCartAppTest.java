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
        TestContext ctx = createContext(
                "1\n2\n100.0\n2\n50.0\n3\n",
                "en",
                defaultStrings("Item count", "Price", "Quantity", "Cart total")
        );

        ShoppingCartApp.run(ctx.scanner, ctx.out, ctx.localizationService, ctx.cartService);

        verify(ctx.localizationService).getStrings("en");
        verify(ctx.cartService).saveCart(eq(2), eq(350.0), eq("en"), anyList());

        String output = ctx.outputStream.toString();
        assertTrue(output.contains("Select language:"));
        assertTrue(output.contains("Enter choice:"));
        assertTrue(output.contains("Cart total: 350.0"));
    }

    @Test
    void runShouldSaveCorrectItems() {
        TestContext ctx = createContext(
                "1\n2\n10.0\n2\n5.0\n4\n",
                "en",
                defaultStrings("Item count", "Price", "Quantity", "Cart total")
        );

        ShoppingCartApp.run(ctx.scanner, ctx.out, ctx.localizationService, ctx.cartService);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<CartItem>> itemsCaptor = ArgumentCaptor.forClass(List.class);

        verify(ctx.cartService).saveCart(eq(2), eq(40.0), eq("en"), itemsCaptor.capture());

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
    void runShouldHandleArabic() {
        TestContext ctx = createContext(
                "5\n1\n20.0\n2\n",
                "ar",
                defaultStrings("عدد العناصر", "السعر", "الكمية", "المجموع")
        );

        ShoppingCartApp.run(ctx.scanner, ctx.out, ctx.localizationService, ctx.cartService);

        verify(ctx.localizationService).getStrings("ar");
        verify(ctx.cartService).saveCart(eq(1), eq(40.0), eq("ar"), anyList());

        String output = ctx.outputStream.toString();
        assertTrue(output.contains("المجموع: 40.0"));
    }

    @Test
    void runShouldHandleZeroItems() {
        TestContext ctx = createContext(
                "1\n0\n",
                "en",
                defaultStrings("Item count", "Price", "Quantity", "Cart total")
        );

        ShoppingCartApp.run(ctx.scanner, ctx.out, ctx.localizationService, ctx.cartService);

        verify(ctx.localizationService).getStrings("en");
        verify(ctx.cartService).saveCart(eq(0), eq(0.0), eq("en"), anyList());

        String output = ctx.outputStream.toString();
        assertTrue(output.contains("Cart total: 0.0"));
    }

    private TestContext createContext(String input, String language, Map<String, String> strings) {
        TestContext ctx = new TestContext();
        ctx.scanner = new Scanner(input);
        ctx.outputStream = new ByteArrayOutputStream();
        ctx.out = new PrintStream(ctx.outputStream);
        ctx.localizationService = mock(LocalizationService.class);
        ctx.cartService = mock(CartService.class);

        when(ctx.localizationService.getStrings(language)).thenReturn(strings);
        return ctx;
    }

    private Map<String, String> defaultStrings(String itemCount, String price, String quantity, String cartTotal) {
        Map<String, String> strings = new HashMap<>();
        strings.put("itemCount", itemCount);
        strings.put("price", price);
        strings.put("quantity", quantity);
        strings.put("cartTotal", cartTotal);
        return strings;
    }

    private static class TestContext {
        Scanner scanner;
        ByteArrayOutputStream outputStream;
        PrintStream out;
        LocalizationService localizationService;
        CartService cartService;
    }
}