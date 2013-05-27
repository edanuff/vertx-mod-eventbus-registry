package org.usergrid.vx.eventbus_registry;

import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;
import org.vertx.java.platform.Verticle;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class Registry extends Verticle implements RegistryMBean {

  private static final Logger log = LoggerFactory.getLogger(Registry.class);

    public static final long DEFAULT_EXPIRATION_AGE = 5000;
    public static final long DEFAULT_PING_TIME = 1000;
    public static final long DEFAULT_SWEEP_TIME = 0;
    // Our own addresses
    public static final String EVENTBUS_REGISTRY_EXPIRED = "eventbus.registry.expired";
    public static final String EVENTBUS_REGISTRY_PING = "eventbus.registry.ping";
    public static final String EVENTBUS_REGISTRY_SEARCH = "eventbus.registry.search";
    public static final String EVENTBUS_REGISTRY_GET = "eventbus.registry.get";
    public static final String EVENTBUS_REGISTRY_REGISTER = "eventbus.registry.register";
    // Config properties
    public static final String CONFIG_EXPIRATION = "expiration";
    public static final String CONFIG_PING = "ping";
    public static final String CONFIG_SWEEP = "sweep";

    private Map<String, Long> handlers;

    long expiration_age = DEFAULT_EXPIRATION_AGE;
    long ping_time = DEFAULT_PING_TIME;
    long sweep_time = DEFAULT_SWEEP_TIME;

    @Override
    public void start() {
        log.info("EventBus registry Verticle starting");
        // acquire (or create) registry map
        handlers = vertx.sharedData().getMap("eventbus.registry");
        // setup
        maybeRegisterMBean();

        doConfiguration();

        createHandlers();


        if (ping_time > 0) {
            vertx.setPeriodic(ping_time, new Handler<Long>() {
                @Override
                public void handle(Long timerID) {
                    vertx.eventBus().publish(EVENTBUS_REGISTRY_PING,
                            System.currentTimeMillis());
                }
            });
        }

        if ((expiration_age > 0) && (sweep_time > 0)) {
            vertx.setPeriodic(sweep_time, new Handler<Long>() {
                @Override
                public void handle(Long timerID) {
                    long expired = System.currentTimeMillis() - expiration_age;

                    Iterator<Entry<String, Long>> it = handlers.entrySet()
                            .iterator();

                    while (it.hasNext()) {
                        Entry<String, Long> entry = it.next();
                        if ((entry.getValue() == null)
                                || (entry.getValue().longValue() < expired)) {
                            // vertx's SharedMap instances returns a copy internally, so we must remove by hand
                            handlers.remove(entry.getKey());
                            vertx.eventBus()
                            .publish(EVENTBUS_REGISTRY_EXPIRED,
                                    entry.getKey());
                        }
                    }
                }
            });
        }
    }

    @Override
    public void stop() {
        log.info("EventBus registry stopped");
    }

    @Override
    public Map<String, Long> getHandlers() {
      return Collections.unmodifiableMap(handlers);
    }

    @Override
    public void expireHandler(String address) {
      Long removed = handlers.remove(address);
      if ( removed != null ) {
        log.info(String.format("Explicit expiration for %s was %d", address, removed));
        vertx.eventBus().publish(EVENTBUS_REGISTRY_EXPIRED, address);
      }
    }

   private void createHandlers() {
     // REGISTER handler
     vertx.eventBus().registerHandler(EVENTBUS_REGISTRY_REGISTER,
             new RegisterHandler(handlers));

     // GET handler
     vertx.eventBus().registerHandler(EVENTBUS_REGISTRY_GET,
             new GetHandler(handlers, expiration_age));

     // SEARCH handler
     vertx.eventBus().registerHandler(EVENTBUS_REGISTRY_SEARCH,
             new SearchHandler(handlers, expiration_age));
   }

   private void doConfiguration() {
     JsonObject config = container.config();
     expiration_age = config.getLong(CONFIG_EXPIRATION,
             DEFAULT_EXPIRATION_AGE);
     ping_time = config.getLong(CONFIG_PING,
             DEFAULT_PING_TIME);
     sweep_time = config.getLong(CONFIG_SWEEP,
             DEFAULT_SWEEP_TIME);
   }

    private void maybeRegisterMBean() {
      MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
      try {
        log.debug("Attaching EventBus Registry to JMX");
        mbs.registerMBean(this, new ObjectName("org.usergrid.vx.eventbus_registry:type=Registry"));
      } catch(InstanceAlreadyExistsException iaee) {
        log.info("EventBus Registry has already been registered with JMX. To be expected with multiple instances of Eventbus Registry");
      } catch(Exception ex) {
        log.error(ex);
      }
    }
}
