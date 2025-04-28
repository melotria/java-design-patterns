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
package com.iluwatar.remotefacade.server.facade;

import com.iluwatar.remotefacade.dto.ProductDto;
import java.util.Optional;

/**
 * ProductRemoteFacade is the Remote Facade interface that provides a coarse-grained interface
 * to the fine-grained services in the product inventory system. This interface would typically
 * be exposed to remote clients through some remote communication mechanism (e.g., RMI, web services).
 */
public interface ProductRemoteFacade {
  
  /**
   * Adds a new product to the inventory.
   *
   * @param name  the name of the product
   * @param price the price of the product
   * @param stock the initial stock of the product
   * @return true if the product was added successfully, false otherwise
   */
  boolean addProduct(String name, double price, int stock);
  
  /**
   * Gets the details of a product.
   *
   * @param name the name of the product
   * @return an Optional containing the product DTO if found, or empty if not found
   */
  Optional<ProductDto> getProductDetails(String name);
  
  /**
   * Updates the price of a product.
   *
   * @param name  the name of the product
   * @param price the new price
   * @return true if the price was updated successfully, false otherwise
   */
  boolean updateProductPrice(String name, double price);
  
  /**
   * Updates the stock of a product.
   *
   * @param name  the name of the product
   * @param stock the new stock
   * @return true if the stock was updated successfully, false otherwise
   */
  boolean updateProductStock(String name, int stock);
  
  /**
   * Processes an order for a product.
   *
   * @param productName the name of the product
   * @param quantity    the quantity to order
   * @return true if the order was processed successfully, false otherwise
   */
  boolean processOrder(String productName, int quantity);
}