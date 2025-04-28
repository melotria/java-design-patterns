/*
 * This project is licensed under the MIT license. Module model-view-viewmodel is using ZK framework licensed under LGPL (see lgpl-3.0.txt).
 *
 * The MIT License
 * Copyright © 2014-2022 Ilkka Seppälä
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.iluwatar.remotefacade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.iluwatar.remotefacade.client.Client;
import com.iluwatar.remotefacade.dto.ProductDto;
import com.iluwatar.remotefacade.server.Server;
import com.iluwatar.remotefacade.server.facade.ProductRemoteFacade;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Remote Facade pattern implementation.
 */
class AppTest {
  private Server server;
  private ProductRemoteFacade remoteFacade;

  @BeforeEach
  void setUp() {
    server = new Server();
    server.start();
    remoteFacade = server.getRemoteFacade();
  }

  @AfterEach
  void tearDown() {
    server.stop();
  }

  @Test
  void testAddProduct() {
    // Add a product
    boolean result = remoteFacade.addProduct("TestProduct", 100.0, 10);
    assertTrue(result);

    // Try to add the same product again
    result = remoteFacade.addProduct("TestProduct", 200.0, 20);
    assertFalse(result);
  }

  @Test
  void testGetProductDetails() {
    // Add a product
    remoteFacade.addProduct("TestProduct", 100.0, 10);

    // Get the product details
    Optional<ProductDto> productOpt = remoteFacade.getProductDetails("TestProduct");
    assertTrue(productOpt.isPresent());
    ProductDto product = productOpt.get();
    assertEquals("TestProduct", product.getName());
    assertEquals(100.0, product.getPrice());
    assertEquals(10, product.getStock());

    // Try to get a non-existent product
    productOpt = remoteFacade.getProductDetails("NonExistentProduct");
    assertFalse(productOpt.isPresent());
  }

  @Test
  void testUpdateProductPrice() {
    // Add a product
    remoteFacade.addProduct("TestProduct", 100.0, 10);

    // Update the price
    boolean result = remoteFacade.updateProductPrice("TestProduct", 200.0);
    assertTrue(result);

    // Verify the price was updated
    Optional<ProductDto> productOpt = remoteFacade.getProductDetails("TestProduct");
    assertTrue(productOpt.isPresent());
    assertEquals(200.0, productOpt.get().getPrice());

    // Try to update the price of a non-existent product
    result = remoteFacade.updateProductPrice("NonExistentProduct", 300.0);
    assertFalse(result);
  }

  @Test
  void testUpdateProductStock() {
    // Add a product
    remoteFacade.addProduct("TestProduct", 100.0, 10);

    // Update the stock
    boolean result = remoteFacade.updateProductStock("TestProduct", 20);
    assertTrue(result);

    // Verify the stock was updated
    Optional<ProductDto> productOpt = remoteFacade.getProductDetails("TestProduct");
    assertTrue(productOpt.isPresent());
    assertEquals(20, productOpt.get().getStock());

    // Try to update the stock of a non-existent product
    result = remoteFacade.updateProductStock("NonExistentProduct", 30);
    assertFalse(result);
  }

  @Test
  void testProcessOrder() {
    // Add a product
    remoteFacade.addProduct("TestProduct", 100.0, 10);

    // Process an order
    boolean result = remoteFacade.processOrder("TestProduct", 5);
    assertTrue(result);

    // Verify the stock was decreased
    Optional<ProductDto> productOpt = remoteFacade.getProductDetails("TestProduct");
    assertTrue(productOpt.isPresent());
    assertEquals(5, productOpt.get().getStock());

    // Try to process an order for more than the available stock
    result = remoteFacade.processOrder("TestProduct", 10);
    assertFalse(result);

    // Try to process an order for a non-existent product
    result = remoteFacade.processOrder("NonExistentProduct", 5);
    assertFalse(result);
  }

  @Test
  void testClientUsage() {
    // Create a client
    Client client = new Client(remoteFacade);

    // Add a product
    client.addProduct("TestProduct", 100.0, 10);

    // Get the product details
    Optional<ProductDto> productOpt = remoteFacade.getProductDetails("TestProduct");
    assertTrue(productOpt.isPresent());
    assertEquals("TestProduct", productOpt.get().getName());
    assertEquals(100.0, productOpt.get().getPrice());
    assertEquals(10, productOpt.get().getStock());

    // Update the price
    client.updateProductPrice("TestProduct", 200.0);
    productOpt = remoteFacade.getProductDetails("TestProduct");
    assertTrue(productOpt.isPresent());
    assertEquals(200.0, productOpt.get().getPrice());

    // Update the stock
    client.updateProductStock("TestProduct", 20);
    productOpt = remoteFacade.getProductDetails("TestProduct");
    assertTrue(productOpt.isPresent());
    assertEquals(20, productOpt.get().getStock());

    // Process an order
    client.processOrder("TestProduct", 5);
    productOpt = remoteFacade.getProductDetails("TestProduct");
    assertTrue(productOpt.isPresent());
    assertEquals(15, productOpt.get().getStock());
  }
}