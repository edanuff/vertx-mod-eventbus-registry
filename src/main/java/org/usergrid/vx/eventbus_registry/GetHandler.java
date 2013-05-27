package org.usergrid.vx.eventbus_registry;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;

import java.util.Map;

/**
 * Handles the GET operation for EventBus registry
 * @author zznate
 */
public class GetHandler implements Handler<Message<String>>{

  private final long expiration_age;
  private final Map<String, Long> handlers;

  GetHandler(Map<String, Long> handlers, long expiration_age) {
    this.handlers = handlers;
    this.expiration_age = expiration_age;
  }

  @Override
  public void handle(Message<String> message) {
    message.reply(doHandler(message.body()));
  }

  boolean doHandler(String message) {
    // register time
    Long age = handlers.get(message);
    if ((expiration_age > 0) && (age != null)) {
      // verify age + expiration age is still greater than now
      return System.currentTimeMillis() < (age.longValue() + expiration_age);
    }
    // on no expiration, simpy verify we are registered
    return age != null;
  }

}
