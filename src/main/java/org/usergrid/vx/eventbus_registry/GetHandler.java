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
    return RegistryUtils.isExpired(age, expiration_age);
  }


}
