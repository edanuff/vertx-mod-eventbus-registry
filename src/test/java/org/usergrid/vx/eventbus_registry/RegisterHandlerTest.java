package org.usergrid.vx.eventbus_registry;

import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;

/**
 * Verify simple test to verify/demonstrate behavior of register semantics
 *
 */
public class RegisterHandlerTest {


  private static final String ADDRESS =
          "org.usergrid.my.fake.address";

  @Test
  public void verifyRegisterOk() {
    RegisterHandler registerHandler = new RegisterHandler(new HashMap<String,Long>());
    Long ts = registerHandler.doHandle(ADDRESS);
    assertNull(ts);
    ts = registerHandler.doHandle(ADDRESS);
    assertNotNull(ts);
    assertNotSame(ts, registerHandler.doHandle(ADDRESS));
  }
}
