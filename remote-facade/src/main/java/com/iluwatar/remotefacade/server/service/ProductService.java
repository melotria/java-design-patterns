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

import com.iluwatar.remotefacade.server.model.Product;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * ProductService is a fine-grained service that provides operations for managing products.
 * This is part of the server-side service layer and is not directly exposed to clients.
 */
@Slf4j
public class ProductService {
  private final Map<String, Product> products = new HashMap<>();

  /**
   * Adds a new product to the inventory.
   *
   * @param name  the name of the product
   * @param price the price of the product
   * @param stock the initial stock of the product
   * @return true if the product was added successfully, false otherwise
   */
  public boolean addProduct(String name, double price, int stock) {
    if (products.containsKey(name)) {
      LOGGER.info("Product {} already exists", name);
      return false;
    }
    
    products.put(name, new Product(name, price, stock));
    LOGGER.info("Product {} added with price {} and stock {}", name, price, stock);
    return true;
  }

  /**
   * Gets a product by name.
   *
   * @param name the name of the product
   * @return an Optional containing the product if found, or empty if not found
   */
  public Optional<Product> getProduct(String name) {
    var product = products.get(name);
    if (product == null) {
      LOGGER.info("Product {} not found", name);
      return Optional.empty();
    }
    
    LOGGER.info("Retrieved product {}: price={}, stock={}", 
        name, product.getPrice(), product.getStock());
    return Optional.of(product);
  }

  /**
   * Updates the price of a product.
   *
   * @param name  the name of the product
   * @param price the new price
   * @return true if the price was updated successfully, false otherwise
   */
  public boolean updatePrice(String name, double price) {
    var product = products.get(name);
    if (product == null) {
      LOGGER.info("Cannot update price: Product {} not found", name);
      return false;
    }
    
    product.setPrice(price);
    LOGGER.info("Updated price of product {} to {}", name, price);
    return true;
  }

  /**
   * Updates the stock of a product.
   *
   * @param name  the name of the product
   * @param stock the new stock
   * @return true if the stock was updated successfully, false otherwise
   */
  public boolean updateStock(String name, int stock) {
    var product = products.get(name);
    if (product == null) {
      LOGGER.info("Cannot update stock: Product {} not found", name);
      return false;
    }
    
    product.setStock(stock);
    LOGGER.info("Updated stock of product {} to {}", name, stock);
    return true;
  }

  /**
   * Decreases the stock of a product by the specified amount.
   *
   * @param name   the name of the product
   * @param amount the amount to decrease
   * @return true if the stock was decreased successfully, false otherwise
   */
  public boolean decreaseStock(String name, int amount) {
    var product = products.get(name);
    if (product == null) {
      LOGGER.info("Cannot decrease stock: Product {} not found", name);
      return false;
    }
    
    if (product.getStock() < amount) {
      LOGGER.info("Cannot decrease stock: Not enough stock for product {}", name);
      return false;
    }
    
    product.setStock(product.getStock() - amount);
    LOGGER.info("Decreased stock of product {} by {}. New stock: {}", 
        name, amount, product.getStock());
    return true;
  }
}