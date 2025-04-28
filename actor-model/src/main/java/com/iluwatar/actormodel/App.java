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

/**
 * The Actor Model is a design pattern used to handle concurrency in a safe, scalable, and
 * message-driven way.
 *
 * <p>In the Actor Model: - An **Actor** is an independent unit that has its own state and behavior.
 * - Actors **communicate only through messages** — they do not share memory. - An **ActorSystem**
 * is responsible for creating, starting, and managing the lifecycle of actors. - Messages are
 * delivered asynchronously, and each actor processes them one at a time.
 *
 * <p>💡 Key benefits: - No shared memory = no need for complex thread-safety - Easy to scale with
 * many actors - Suitable for highly concurrent or distributed systems
 *
 * <p>🔍 This example demonstrates the Actor Model: - `ActorSystem` starts two actors: `srijan` and
 * `ansh`. - `ExampleActor` and `ExampleActor2` extend the `Actor` class and override the
 * `onReceive()` method to handle messages. - Actors communicate using `send()` to pass `Message`
 * objects that include the message content and sender's ID. - The actors process messages
 * **asynchronously in separate threads**, and we allow a short delay (`Thread.sleep`) to let them
 * run. - The system is shut down gracefully at the end.
 */
package com.iluwatar.actormodel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
  /**
   * Main method to demonstrate the Actor Model pattern.
   * 
   * @param args command line arguments (not used)
   * @throws InterruptedException if thread is interrupted
   */
  public static void main(String[] args) throws InterruptedException {
    // Create the actor system
    ActorSystem system = new ActorSystem();

    // Create and start parent actors
    ExampleActor parent = new ExampleActor(system);
    String parentId = system.startActor(parent);
    LOGGER.info("Started parent actor with ID: {}", parentId);

    // Parent creates a child actor
    String childId = parent.createChildExampleActor();
    LOGGER.info("Parent created child actor with ID: {}", childId);

    // Get the child actor
    ExampleActor2 child = (ExampleActor2) system.getActorById(childId);

    // Basic message passing
    child.send(new Message("Hello from parent", parent.getActorId()));
    parent.send(new Message("Hello from child", child.getActorId()));

    Thread.sleep(500); // Give time for messages to process

    // Demonstrate supervision - send a message that will cause an error
    LOGGER.info("Sending message that will cause an error to demonstrate supervision");
    child.send(new Message("This will cause an error", parent.getActorId()));

    Thread.sleep(1000); // Give time for error handling and supervision

    // Create another actor directly through the system
    Actor anotherActor = new ExampleActor2(system);
    system.startActor(anotherActor);
    anotherActor.send(new Message("Hello from the system", "system"));

    Thread.sleep(500); // Give time for messages to process

    // Graceful shutdown
    LOGGER.info("Shutting down actors and system");
    parent.stop();
    child.stop();
    anotherActor.stop();
    system.shutdown();
  }
}
