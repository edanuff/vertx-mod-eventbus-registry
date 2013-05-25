package org.usergrid.vx.eventbus_registry;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;

import java.util.Map;

/**
 * @author zznate
 */
public class EventBusRegistryGetHandler implements Handler<Message<String>>{

  private final Long expiration_age;
  private final Map<String, Long> handlers;

  EventBusRegistryGetHandler(Map<String,Long> handlers, long expiration_age) {
    this.handlers = handlers;
    this.expiration_age = Long.valueOf(expiration_age);
  }

  @Override
  public void handle(Message<String> message) {
    message.reply(doHandler(message.body()));
  }

  boolean doHandler(String message) {
    Long age = handlers.get(message);
    if ((expiration_age > 0) && (age != null)) {
      return age < (System.currentTimeMillis() - expiration_age);
    }
    return age != null;
  }

}
