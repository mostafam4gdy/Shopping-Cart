package com.reljicd.service.impl;

import com.reljicd.exception.NotEnoughProductsInStockException;
import com.reljicd.model.Product;
import com.reljicd.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ShoppingCartServiceImplTest {

    private ProductRepository productRepository;
    private ShoppingCartServiceImpl shoppingCart;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        shoppingCart = new ShoppingCartServiceImpl(productRepository);
    }

    @Test
    void addProductIncrementsQuantityForExistingProduct() {
        Product product = product(1L, 10, "2.50");

        shoppingCart.addProduct(product);
        shoppingCart.addProduct(product);

        assertEquals(2, shoppingCart.getProductsInCart().get(product));
    }

    @Test
    void removeProductDecrementsAndThenRemovesProduct() {
        Product product = product(1L, 10, "2.50");
        shoppingCart.addProduct(product);
        shoppingCart.addProduct(product);

        shoppingCart.removeProduct(product);
        assertEquals(1, shoppingCart.getProductsInCart().get(product));

        shoppingCart.removeProduct(product);
        shoppingCart.removeProduct(product);
        assertTrue(shoppingCart.getProductsInCart().isEmpty());
    }

    @Test
    void productsInCartCannotBeModifiedThroughReturnedMap() {
        Product product = product(1L, 10, "2.50");
        shoppingCart.addProduct(product);

        assertThrows(UnsupportedOperationException.class,
                () -> shoppingCart.getProductsInCart().clear());
    }

    @Test
    void totalIsZeroForEmptyCartAndSumsProductQuantities() {
        assertEquals(BigDecimal.ZERO, shoppingCart.getTotal());

        Product first = product(1L, 10, "2.50");
        Product second = product(2L, 10, "3.25");
        shoppingCart.addProduct(first);
        shoppingCart.addProduct(first);
        shoppingCart.addProduct(second);

        assertEquals(new BigDecimal("8.25"), shoppingCart.getTotal());
    }

    @Test
    void checkoutUsesCurrentStockAndClearsCart() throws NotEnoughProductsInStockException {
        Product cartProduct = product(1L, 20, "2.50");
        Product currentProduct = product(1L, 7, "2.50");
        shoppingCart.addProduct(cartProduct);
        shoppingCart.addProduct(cartProduct);
        when(productRepository.findById(1L)).thenReturn(Optional.of(currentProduct));

        shoppingCart.checkout();

        assertEquals(5, cartProduct.getQuantity());
        assertTrue(shoppingCart.getProductsInCart().isEmpty());
        verify(productRepository).saveAll(anyIterable());
        verify(productRepository).flush();
    }

    @Test
    void checkoutRejectsInsufficientStockAndKeepsCart() {
        Product cartProduct = product(1L, 10, "2.50");
        Product currentProduct = product(1L, 0, "2.50");
        shoppingCart.addProduct(cartProduct);
        when(productRepository.findById(1L)).thenReturn(Optional.of(currentProduct));

        NotEnoughProductsInStockException exception = assertThrows(
                NotEnoughProductsInStockException.class, shoppingCart::checkout);

        assertEquals("Not enough Test product products in stock. Only 0 left", exception.getMessage());
        assertEquals(1, shoppingCart.getProductsInCart().get(cartProduct));
        verify(productRepository, never()).saveAll(anyIterable());
        verify(productRepository, never()).flush();
    }

    @Test
    void checkoutWithEmptyCartDoesNotFail() throws NotEnoughProductsInStockException {
        shoppingCart.checkout();

        verify(productRepository).saveAll(anyIterable());
        verify(productRepository).flush();
    }

    private Product product(Long id, int quantity, String price) {
        Product product = new Product();
        product.setId(id);
        product.setName("Test product");
        product.setQuantity(quantity);
        product.setPrice(new BigDecimal(price));
        return product;
    }
}
