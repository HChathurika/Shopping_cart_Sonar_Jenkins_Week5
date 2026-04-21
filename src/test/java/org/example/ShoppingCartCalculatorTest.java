package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShoppingCartCalculatorTest {

    @Test
    void calculateItemTotalShouldReturnCorrectValue() {
        double result = ShoppingCartCalculator.calculateItemTotal(100.0, 3);
        assertEquals(300.0, result);
    }

    @Test
    void calculateItemTotalShouldReturnZeroWhenQuantityIsZero() {
        double result = ShoppingCartCalculator.calculateItemTotal(100.0, 0);
        assertEquals(0.0, result);
    }

    @Test
    void calculateItemTotalShouldReturnZeroWhenPriceIsZero() {
        double result = ShoppingCartCalculator.calculateItemTotal(0.0, 5);
        assertEquals(0.0, result);
    }

    @Test
    void calculateCartTotalShouldReturnCorrectSumForMultipleItems() {
        double[] prices = {100.0, 50.0, 25.0};
        int[] quantities = {2, 3, 4};

        double result = ShoppingCartCalculator.calculateCartTotal(prices, quantities);

        assertEquals(450.0, result);
    }

    @Test
    void calculateCartTotalShouldReturnZeroForEmptyArrays() {
        double[] prices = {};
        int[] quantities = {};

        double result = ShoppingCartCalculator.calculateCartTotal(prices, quantities);

        assertEquals(0.0, result);
    }

    @Test
    void calculateCartTotalShouldWorkForSingleItem() {
        double[] prices = {199.99};
        int[] quantities = {2};

        double result = ShoppingCartCalculator.calculateCartTotal(prices, quantities);

        assertEquals(399.98, result);
    }

    @Test
    void calculateItemTotalShouldWorkWithNegativePrice() {
        double result = ShoppingCartCalculator.calculateItemTotal(-10.0, 2);
        assertEquals(-20.0, result);
    }

    @Test
    void calculateCartTotalShouldWorkWithZeroAndNegativeValues() {
        double[] prices = {10.0, 0.0, -5.0};
        int[] quantities = {2, 3, 4};

        double result = ShoppingCartCalculator.calculateCartTotal(prices, quantities);

        assertEquals(0.0, result);
    }
}