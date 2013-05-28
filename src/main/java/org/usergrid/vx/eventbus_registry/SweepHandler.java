package org.usergrid.vx.eventbus_registry;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;

import java.util.Iterator;
import java.util.Map;

/**
 * @author zznate
 */
public class SweepHandler extends GetHandler {

  private final Vertx vertx;
  // TODO sweep needs to be Handler<Long> as it is a 'periodic', so can't extend GetHandler
  // TODO expriation check semantics need to live in utility class

  SweepHandler(Vertx vertx, Map<String,Long> handlers, long expiration_age) {
    super(handlers,expiration_age);
    this.vertx = vertx;
  }

  @Override
  public void handle(Message<String> message) {
    // TODO do we need to override? or use utils method?
    // this feels a bit clunky currently

    Iterator<Map.Entry<String, Long>> it = handlers.entrySet()
            .iterator();

    while (it.hasNext()) {
      Map.Entry<String, Long> entry = it.next();
      if ((entry.getValue() == null)
              || isExpired(entry.getValue().longValue() )) {
        // vertx's SharedMap instances returns a copy internally, so we must remove by hand
        handlers.remove(entry.getKey());
        vertx.eventBus()
                .publish(Registry.EVENTBUS_REGISTRY_EXPIRED,
                        entry.getKey());
      }
    }
  }

}
