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
package com.iluwatar.remotefacade.server.service;

import lombok.extern.slf4j.Slf4j;

/**
 * OrderService is a fine-grained service that provides operations for processing orders.
 * This is part of the server-side service layer and is not directly exposed to clients.
 */
@Slf4j
public class OrderService {
  private final ProductService productService;

  /**
   * Constructs an OrderService with the specified ProductService.
   *
   * @param productService the product service to use
   */
  public OrderService(ProductService productService) {
    this.productService = productService;
  }

  /**
   * Processes an order for a product.
   *
   * @param productName the name of the product
   * @param quantity    the quantity to order
   * @return true if the order was processed successfully, false otherwise
   */
  public boolean processOrder(String productName, int quantity) {
    LOGGER.info("Processing order for {} units of product {}", quantity, productName);
    
    // Check if the product exists and has enough stock
    var productOpt = productService.getProduct(productName);
    if (productOpt.isEmpty()) {
      LOGGER.info("Order processing failed: Product {} not found", productName);
      return false;
    }
    
    var product = productOpt.get();
    if (product.getStock() < quantity) {
      LOGGER.info("Order processing failed: Not enough stock for product {}", productName);
      return false;
    }
    
    // Decrease the stock
    if (!productService.decreaseStock(productName, quantity)) {
      LOGGER.info("Order processing failed: Could not decrease stock for product {}", productName);
      return false;
    }
    
    // Calculate the total price
    double totalPrice = product.getPrice() * quantity;
    LOGGER.info("Order processed successfully: {} units of product {} for a total of ${}", 
        quantity, productName, totalPrice);
    
    return true;
  }
}