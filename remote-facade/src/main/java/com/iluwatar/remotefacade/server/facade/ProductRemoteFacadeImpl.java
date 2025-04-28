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
import com.iluwatar.remotefacade.server.model.Product;
import com.iluwatar.remotefacade.server.service.OrderService;
import com.iluwatar.remotefacade.server.service.ProductService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * ProductRemoteFacadeImpl is the implementation of the Remote Facade interface.
 * It coordinates the fine-grained services to provide a coarse-grained interface to clients.
 * This is a key component of the Remote Facade pattern as it encapsulates the complexity
 * of the subsystem and provides a simplified interface for remote clients.
 */
@Slf4j
public class ProductRemoteFacadeImpl implements ProductRemoteFacade {
  private final ProductService productService;
  private final OrderService orderService;

  /**
   * Constructs a ProductRemoteFacadeImpl with the specified services.
   *
   * @param productService the product service
   * @param orderService   the order service
   */
  public ProductRemoteFacadeImpl(ProductService productService, OrderService orderService) {
    this.productService = productService;
    this.orderService = orderService;
  }

  @Override
  public boolean addProduct(String name, double price, int stock) {
    LOGGER.info("Remote facade: Adding product {}", name);
    return productService.addProduct(name, price, stock);
  }

  @Override
  public Optional<ProductDto> getProductDetails(String name) {
    LOGGER.info("Remote facade: Getting details for product {}", name);
    return productService.getProduct(name)
        .map(this::convertToDto);
  }

  @Override
  public boolean updateProductPrice(String name, double price) {
    LOGGER.info("Remote facade: Updating price for product {}", name);
    return productService.updatePrice(name, price);
  }

  @Override
  public boolean updateProductStock(String name, int stock) {
    LOGGER.info("Remote facade: Updating stock for product {}", name);
    return productService.updateStock(name, stock);
  }

  @Override
  public boolean processOrder(String productName, int quantity) {
    LOGGER.info("Remote facade: Processing order for product {}", productName);
    return orderService.processOrder(productName, quantity);
  }

  /**
   * Converts a Product entity to a ProductDto.
   *
   * @param product the product entity
   * @return the product DTO
   */
  private ProductDto convertToDto(Product product) {
    return new ProductDto(product.getName(), product.getPrice(), product.getStock());
  }
}