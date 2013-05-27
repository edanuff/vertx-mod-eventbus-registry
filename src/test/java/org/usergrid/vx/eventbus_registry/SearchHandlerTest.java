package org.usergrid.vx.eventbus_registry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.vertx.java.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class SearchHandlerTest {

  private Map<String,Long> handlers;

  @Before
  public void setupLocal() {
    handlers = new HashMap<>();
  }

  @Test
  public void verifySearchPatternsOk() {
    long now = System.currentTimeMillis();
    handlers.put("aaaab",now);
    handlers.put("aab",now);
    handlers.put("Foo",now);

    SearchHandler searchHandler = new SearchHandler(handlers,
            Registry.DEFAULT_EXPIRATION_AGE*100);

    // find by pattern
    JsonObject jo = searchHandler.doSearch("a*b");
    Assert.assertEquals(2, jo.getFieldNames().size());
    // all
    jo = searchHandler.doSearch(".*");
    Assert.assertEquals(3, jo.getFieldNames().size());
    // miss
    jo = searchHandler.doSearch("\\s");
    Assert.assertEquals(0, jo.getFieldNames().size());
  }
}
