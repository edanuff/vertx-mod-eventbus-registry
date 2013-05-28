package org.usergrid.vx.eventbus_registry;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;

import java.util.Map;

/**
 * Handles the GET operation for EventBus registry
 * @author zznate
 */
public class GetHandler implements Handler<Message<String>>{

  protected final long expiration_age;
  protected final Map<String, Long> handlers;

  GetHandler(Map<String, Long> handlers, long expiration_age) {
    this.handlers = handlers;
    this.expiration_age = expiration_age;
  }

  @Override
  public void handle(Message<String> message) {
    message.reply(doHandle(message.body()));
  }

  boolean doHandle(String address) {
    // register time
    Long age = handlers.get(address);
    return isExpired(age);
  }

  // TODO need to move this into a utility class
  boolean isExpired(Long age) {
    if ((expiration_age > 0) && (age != null)) {
      // verify age + expiration age is still greater than now
      return System.currentTimeMillis() < (age + expiration_age);
    }
    // on no expiration, simpy verify we are registered
    return age != null;
  }

}
