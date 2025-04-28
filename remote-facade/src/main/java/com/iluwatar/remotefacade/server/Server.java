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
package com.iluwatar.remotefacade.server;

import com.iluwatar.remotefacade.server.facade.ProductRemoteFacade;
import com.iluwatar.remotefacade.server.facade.ProductRemoteFacadeImpl;
import com.iluwatar.remotefacade.server.service.OrderService;
import com.iluwatar.remotefacade.server.service.ProductService;
import lombok.extern.slf4j.Slf4j;

/**
 * Server represents the server-side of the application.
 * It sets up the services and facade, and provides a method to get the remote facade.
 * In a real-world scenario, this would likely involve setting up a server that exposes
 * the facade through some remote communication mechanism (e.g., RMI, web services).
 */
@Slf4j
public class Server {
  private ProductService productService;
  private OrderService orderService;
  private ProductRemoteFacade remoteFacade;

  /**
   * Starts the server, initializing the services and facade.
   */
  public void start() {
    LOGGER.info("Starting server...");
    
    // Initialize services
    productService = new ProductService();
    orderService = new OrderService(productService);
    
    // Create the remote facade
    remoteFacade = new ProductRemoteFacadeImpl(productService, orderService);
    
    LOGGER.info("Server started successfully");
  }

  /**
   * Stops the server.
   */
  public void stop() {
    LOGGER.info("Stopping server...");
    // In a real-world scenario, this would involve cleaning up resources,
    // closing connections, etc.
    LOGGER.info("Server stopped successfully");
  }

  /**
   * Gets the remote facade that can be used by clients.
   * In a real-world scenario, this would likely involve some form of remote lookup
   * or service discovery.
   *
   * @return the remote facade
   */
  public ProductRemoteFacade getRemoteFacade() {
    if (remoteFacade == null) {
      throw new IllegalStateException("Server not started");
    }
    return remoteFacade;
  }
}