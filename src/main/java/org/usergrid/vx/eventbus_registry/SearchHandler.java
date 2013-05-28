package org.usergrid.vx.eventbus_registry;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Handler to provide Search operation on EventBus registry. Extends
 * {@link GetHandler}, using the underlying 'get' semantics of such for keys found
 * matching the provided pattern.
 *
 */
public class SearchHandler extends GetHandler {

  SearchHandler(Map<String,Long> handlers, long expiration_age) {
    super(handlers, expiration_age);
  }

  @Override
  public void handle(Message<String> message) {
    message.reply(doSearch(message.body()));
  }

  JsonObject doSearch(String message) {
    JsonObject results = new JsonObject();

    Pattern p;
    try {
      p = Pattern.compile(message);
    } catch (PatternSyntaxException pse) {
      results.putString("search_syntax_error",pse.getMessage());
      return results;
    }

    Iterator<Map.Entry<String, Long>> it = handlers.entrySet()
            .iterator();

    while (it.hasNext()) {
      Map.Entry<String, Long> entry = it.next();
      boolean found = doHandle(entry.getKey());
      if ( !found) {
        continue;
      }
      if (p.matcher(entry.getKey()).matches()) {
        results.putNumber(entry.getKey(), entry
                .getValue().longValue());
      }
    }
    return results;
  }


}
