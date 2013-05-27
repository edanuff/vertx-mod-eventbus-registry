package org.usergrid.vx.eventbus_registry;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;

import java.util.Map;

/**
 * Handler for 'Register' operation of the EventBus registry
 *
 */
public class RegisterHandler implements Handler<Message<String>> {
  private static final Logger log = LoggerFactory.getLogger(RegisterHandler.class);

  private final Map<String,Long> handlers;

  RegisterHandler(Map<String,Long> handlers) {
    this.handlers = handlers;
  }

  @Override
  public void handle(Message<String> message) {
    doHandle(message.body());
    message.reply(true);
  }

  Long doHandle(String message) {
    if ( log.isDebugEnabled()) {
      log.debug(String.format("RegisterHandler has message %s", message));
    }
    Long existed = handlers.put(message, System.currentTimeMillis());
    if ( log.isDebugEnabled()) {
      log.debug(String.format("Registry existed for address %s with value %d", message, existed.longValue()));
    }
    return existed;
  }



}
