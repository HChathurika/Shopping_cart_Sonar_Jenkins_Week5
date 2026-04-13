package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartItemTest {

    @Test
    void constructorAndGettersShouldReturnCorrectValues() {
        CartItem item = new CartItem(1, 250.0, 3, 750.0);

        assertEquals(1, item.getItemNumber());
        assertEquals(250.0, item.getPrice());
        assertEquals(3, item.getQuantity());
        assertEquals(750.0, item.getSubtotal());
    }

    @Test
    void constructorAndGettersShouldWorkWithZeroValues() {
        CartItem item = new CartItem(0, 0.0, 0, 0.0);

        assertEquals(0, item.getItemNumber());
        assertEquals(0.0, item.getPrice());
        assertEquals(0, item.getQuantity());
        assertEquals(0.0, item.getSubtotal());
    }

    @Test
    void constructorAndGettersShouldWorkWithDifferentValues() {
        CartItem item = new CartItem(5, 99.99, 2, 199.98);

        assertEquals(5, item.getItemNumber());
        assertEquals(99.99, item.getPrice());
        assertEquals(2, item.getQuantity());
        assertEquals(199.98, item.getSubtotal());
    }
}