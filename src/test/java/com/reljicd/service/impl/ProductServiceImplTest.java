package com.reljicd.service.impl;

import com.reljicd.model.Product;
import com.reljicd.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceImplTest {

    private ProductRepository productRepository;
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void findAllProductsPageableDelegatesToRepository() {
        Pageable pageable = mock(Pageable.class);
        Page<Product> expected = mock(Page.class);
        when(productRepository.findAll(pageable)).thenReturn(expected);

        assertSame(expected, productService.findAllProductsPageable(pageable));
        verify(productRepository).findAll(pageable);
    }

    @Test
    void findByIdReturnsRepositoryResult() {
        Product expected = new Product();
        when(productRepository.findById(42L)).thenReturn(Optional.of(expected));

        assertSame(expected, productService.findById(42L).orElseThrow());
        verify(productRepository).findById(42L);
    }
}
