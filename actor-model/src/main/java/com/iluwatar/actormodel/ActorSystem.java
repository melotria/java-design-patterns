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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ActorSystem {
  private final ExecutorService executor = Executors.newCachedThreadPool();
  private final ConcurrentHashMap<String, Actor> actorRegister = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, String> actorHierarchy = new ConcurrentHashMap<>();
  private final AtomicInteger idCounter = new AtomicInteger(0);

  /**
   * Starts an actor without a parent (top-level actor).
   *
   * @param actor The actor to start
   * @return The ID of the started actor
   */
  public String startActor(Actor actor) {
    String actorId = "actor-" + idCounter.incrementAndGet(); // Generate a new and unique ID
    actor.setActorId(actorId); // assign the actor it's ID
    actorRegister.put(actorId, actor); // Register and save the actor with it's ID
    executor.submit(actor); // Run the actor in a thread
    return actorId;
  }

  /**
   * Starts an actor with a parent actor (child actor).
   * The parent actor will supervise this child actor.
   *
   * @param actor The actor to start
   * @param parentId The ID of the parent actor
   * @return The ID of the started actor
   */
  public String startChildActor(Actor actor, String parentId) {
    String actorId = startActor(actor);
    actorHierarchy.put(actorId, parentId); // Record parent-child relationship
    return actorId;
  }

  /**
   * Gets an actor by its ID.
   *
   * @param actorId The ID of the actor to get
   * @return The actor with the given ID, or null if not found
   */
  public Actor getActorById(String actorId) {
    return actorRegister.get(actorId); //  Find by Id
  }

  /**
   * Gets the parent actor of an actor.
   *
   * @param childId The ID of the child actor
   * @return The parent actor, or null if the actor has no parent
   */
  public Actor getParentActor(String childId) {
    String parentId = actorHierarchy.get(childId);
    return parentId != null ? actorRegister.get(parentId) : null;
  }

  /**
   * Restarts an actor that has failed.
   * If the actor has a parent, the parent will be notified.
   *
   * @param actorId The ID of the actor to restart
   * @param error The error that caused the actor to fail
   */
  public void restartActor(String actorId, Throwable error) {
    Actor actor = actorRegister.get(actorId);
    if (actor != null) {
      // Stop the current actor
      actor.stop();

      // Notify parent if exists
      String parentId = actorHierarchy.get(actorId);
      if (parentId != null) {
        Actor parent = actorRegister.get(parentId);
        if (parent != null) {
          parent.send(new Message("Child actor " + actorId + " failed: " + error.getMessage(), "system"));
        }
      }

      // Restart the actor
      executor.submit(actor);
    }
  }

  /**
   * Shuts down the actor system, stopping all actors.
   */
  public void shutdown() {
    executor.shutdownNow(); // Stop all threads
  }
}
