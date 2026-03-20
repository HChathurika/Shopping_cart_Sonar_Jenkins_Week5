import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShoppingCartAppTest {

    @Test
    void testCalculateItemTotal() {
        assertEquals(50.0, ShoppingCartApp.calculateItemTotal(10.0, 5), 0.001);
    }

    @Test
    void testCalculateItemTotalWithDecimal() {
        assertEquals(22.5, ShoppingCartApp.calculateItemTotal(7.5, 3), 0.001);
    }

    @Test
    void testCalculateCartTotal() {
        double[] prices = {10.0, 20.0, 5.0};
        int[] quantities = {2, 1, 4};
        assertEquals(60.0, ShoppingCartApp.calculateCartTotal(prices, quantities), 0.001);
    }

    @Test
    void testCalculateCartTotalSingleItem() {
        double[] prices = {15.0};
        int[] quantities = {2};
        assertEquals(30.0, ShoppingCartApp.calculateCartTotal(prices, quantities), 0.001);
    }
}