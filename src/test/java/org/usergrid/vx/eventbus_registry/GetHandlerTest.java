package org.usergrid.vx.eventbus_registry;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author zznate
 */
public class GetHandlerTest {

  private Map<String,Long> handlers;

  @Before
  public void doSetup() {
    handlers = new HashMap<>();
  }

  @Test
  public void verifyGetHandlerOk() {
    handlers.put("foo.bar",System.currentTimeMillis());
    GetHandler eventBusRegistryGetHandler = new GetHandler(handlers, new Long(5000));
    assertTrue(eventBusRegistryGetHandler.doHandler("foo.bar"));
  }

  @Test
  public void verifyGetHandlerExpiration() throws Exception {
    handlers.put("expire.verify",System.currentTimeMillis());
    GetHandler eventBusRegistryGetHandler = new GetHandler(handlers, new Long(250));
    TimeUnit.MILLISECONDS.sleep(500);
    assertFalse(eventBusRegistryGetHandler.doHandler("expire.verify"));
  }
}
