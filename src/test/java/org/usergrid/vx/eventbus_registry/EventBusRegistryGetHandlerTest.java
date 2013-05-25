package org.usergrid.vx.eventbus_registry;

import org.junit.Test;
import org.vertx.java.core.eventbus.Message;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * @author zznate
 */
public class EventBusRegistryGetHandlerTest {


  @Test
  public void testGetHandler() {
    Map<String,Long> handlers = new HashMap<>();

    handlers.put("foo.bar",Registry.DEFAULT_EXPIRATION_AGE);

    EventBusRegistryGetHandler eventBusRegistryGetHandler = new EventBusRegistryGetHandler(handlers, new Long(5000));

    assertTrue(eventBusRegistryGetHandler.doHandler("foo.bar"));
  }
}
