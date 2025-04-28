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

import com.iluwatar.remotefacade.client.Client;
import com.iluwatar.remotefacade.server.Server;
import lombok.extern.slf4j.Slf4j;

/**
 * The Remote Facade design pattern is an architectural pattern used to provide a coarse-grained
 * interface to a set of fine-grained objects in a distributed system. This pattern helps to reduce
 * the number of remote calls, thus improving performance and encapsulating the complexities of
 * interactions between remote objects.
 *
 * <p>In this example, we demonstrate the Remote Facade pattern with a simple client-server
 * application. The server contains a set of fine-grained objects (services) that perform various
 * operations on a product inventory. The Remote Facade provides a simplified interface to these
 * services, allowing the client to perform complex operations with fewer remote calls.
 *
 * <p>The key components of this implementation are:
 * <ul>
 * <li>DTOs (Data Transfer Objects): Used to transfer data between client and server</li>
 * <li>Service Layer: Contains the business logic</li>
 * <li>Remote Facade: Provides a simplified interface to the service layer</li>
 * <li>Remote Communication: Simulated through direct method calls</li>
 * </ul>
 */
@Slf4j
public class App {

  /**
   * Program entry point.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    // Start the server
    var server = new Server();
    server.start();

    // Create a client
    var client = new Client(server.getRemoteFacade());

    // Perform operations using the remote facade
    LOGGER.info("Client performing operations using Remote Facade:");
    
    // Add products
    client.addProduct("Laptop", 1200.00, 10);
    client.addProduct("Smartphone", 800.00, 20);
    client.addProduct("Tablet", 500.00, 15);
    
    // Get product details
    client.getProductDetails("Laptop");
    client.getProductDetails("Smartphone");
    
    // Update product price
    client.updateProductPrice("Laptop", 1250.00);
    client.getProductDetails("Laptop");
    
    // Update product stock
    client.updateProductStock("Smartphone", 25);
    client.getProductDetails("Smartphone");
    
    // Process an order
    client.processOrder("Tablet", 5);
    client.getProductDetails("Tablet");
    
    // Stop the server
    server.stop();
  }
}