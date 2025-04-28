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
package com.iluwatar.remotefacade.client;

import com.iluwatar.remotefacade.dto.ProductDto;
import com.iluwatar.remotefacade.server.facade.ProductRemoteFacade;
import lombok.extern.slf4j.Slf4j;

/**
 * Client represents the client-side of the application.
 * It uses the remote facade to interact with the server.
 * In a real-world scenario, this would likely involve making remote calls
 * to the server through some remote communication mechanism (e.g., RMI, web services).
 */
@Slf4j
public class Client {
  private final ProductRemoteFacade remoteFacade;

  /**
   * Constructs a Client with the specified remote facade.
   *
   * @param remoteFacade the remote facade to use
   */
  public Client(ProductRemoteFacade remoteFacade) {
    this.remoteFacade = remoteFacade;
  }

  /**
   * Adds a product to the inventory.
   *
   * @param name  the name of the product
   * @param price the price of the product
   * @param stock the initial stock of the product
   */
  public void addProduct(String name, double price, int stock) {
    LOGGER.info("Client: Adding product {}", name);
    boolean success = remoteFacade.addProduct(name, price, stock);
    if (success) {
      LOGGER.info("Client: Product {} added successfully", name);
    } else {
      LOGGER.info("Client: Failed to add product {}", name);
    }
  }

  /**
   * Gets the details of a product.
   *
   * @param name the name of the product
   */
  public void getProductDetails(String name) {
    LOGGER.info("Client: Getting details for product {}", name);
    var productOpt = remoteFacade.getProductDetails(name);
    if (productOpt.isPresent()) {
      ProductDto product = productOpt.get();
      LOGGER.info("Client: Product details - {}", product);
    } else {
      LOGGER.info("Client: Product {} not found", name);
    }
  }

  /**
   * Updates the price of a product.
   *
   * @param name  the name of the product
   * @param price the new price
   */
  public void updateProductPrice(String name, double price) {
    LOGGER.info("Client: Updating price for product {} to {}", name, price);
    boolean success = remoteFacade.updateProductPrice(name, price);
    if (success) {
      LOGGER.info("Client: Price updated successfully for product {}", name);
    } else {
      LOGGER.info("Client: Failed to update price for product {}", name);
    }
  }

  /**
   * Updates the stock of a product.
   *
   * @param name  the name of the product
   * @param stock the new stock
   */
  public void updateProductStock(String name, int stock) {
    LOGGER.info("Client: Updating stock for product {} to {}", name, stock);
    boolean success = remoteFacade.updateProductStock(name, stock);
    if (success) {
      LOGGER.info("Client: Stock updated successfully for product {}", name);
    } else {
      LOGGER.info("Client: Failed to update stock for product {}", name);
    }
  }

  /**
   * Processes an order for a product.
   *
   * @param productName the name of the product
   * @param quantity    the quantity to order
   */
  public void processOrder(String productName, int quantity) {
    LOGGER.info("Client: Processing order for {} units of product {}", quantity, productName);
    boolean success = remoteFacade.processOrder(productName, quantity);
    if (success) {
      LOGGER.info("Client: Order processed successfully for product {}", productName);
    } else {
      LOGGER.info("Client: Failed to process order for product {}", productName);
    }
  }
}