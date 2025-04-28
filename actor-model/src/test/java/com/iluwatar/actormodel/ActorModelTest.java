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
package com.iluwatar.actormodel;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ActorModelTest {
  @Test
  void testMainMethod() throws InterruptedException {
    App.main(new String[] {});
  }

  @Test
  public void testMessagePassing() throws InterruptedException {
    ActorSystem system = new ActorSystem();

    ExampleActor srijan = new ExampleActor(system);
    ExampleActor2 ansh = new ExampleActor2(system);

    system.startActor(srijan);
    system.startActor(ansh);

    // Ansh receives a message from Srijan
    ansh.send(new Message("Hello ansh", srijan.getActorId()));

    // Wait briefly to allow async processing
    Thread.sleep(200);

    // Check that Ansh received the message
    assertTrue(
        ansh.getReceivedMessages().contains("Hello ansh"),
        "ansh should receive the message from Srijan");
  }

  @Test
  public void testActorCreation() throws InterruptedException {
    ActorSystem system = new ActorSystem();

    // Create parent actor
    ExampleActor parent = new ExampleActor(system);
    system.startActor(parent);

    // Parent creates a child actor
    String childId = parent.createChildExampleActor();

    // Get the child actor
    Actor child = system.getActorById(childId);

    // Verify child was created
    assertNotNull(child, "Child actor should be created");
    assertTrue(child instanceof ExampleActor2, "Child should be an ExampleActor2");

    // Verify parent-child relationship
    Actor parentOfChild = system.getParentActor(childId);
    assertEquals(parent.getActorId(), parentOfChild.getActorId(), 
        "Parent of child should be the parent actor");
  }

  @Test
  public void testSupervision() throws InterruptedException {
    ActorSystem system = new ActorSystem();

    // Create parent actor
    ExampleActor parent = new ExampleActor(system);
    system.startActor(parent);

    // Parent creates a child actor
    String childId = parent.createChildExampleActor();
    ExampleActor2 child = (ExampleActor2) system.getActorById(childId);

    // Send a message that will cause an error
    child.send(new Message("This will cause an error", parent.getActorId()));

    // Wait for error handling and supervision
    Thread.sleep(500);

    // Verify parent received notification about child failure
    boolean parentNotified = parent.getReceivedMessages().stream()
        .anyMatch(msg -> msg.contains("failed"));

    assertTrue(parentNotified, "Parent should be notified about child failure");
  }
}
