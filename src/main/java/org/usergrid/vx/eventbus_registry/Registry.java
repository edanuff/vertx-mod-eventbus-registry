package org.usergrid.vx.eventbus_registry;

import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.HashMap;
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

  Map<String, Long> handlers = new HashMap<String, Long>();

    long expiration_age = DEFAULT_EXPIRATION_AGE;
    long ping_time = DEFAULT_PING_TIME;
    long sweep_time = DEFAULT_SWEEP_TIME;

    @Override
    public void start() {
        log.info("EventBus registry started.");
        maybeRegisterMBean();

        JsonObject config = container.config();
        expiration_age = config.getLong("expiration", DEFAULT_EXPIRATION_AGE);
        ping_time = config.getLong("ping", DEFAULT_PING_TIME);
        sweep_time = config.getLong("sweep", DEFAULT_SWEEP_TIME);

        vertx.eventBus().registerHandler(EVENTBUS_REGISTRY_REGISTER,
                new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> message) {
                handlers.put(message.body(), System.currentTimeMillis());
                log.info("EventBus registered address: " + message.body());
            }
        });

        vertx.eventBus().registerHandler(EVENTBUS_REGISTRY_GET,
                new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> message) {
                Long age = handlers.get(message.body());
                if ((expiration_age > 0) && (age != null)) {
                    message.reply(age < (System.currentTimeMillis() - expiration_age));
                    return;
                }
                message.reply(age != null);
            }
        });

        vertx.eventBus().registerHandler(EVENTBUS_REGISTRY_SEARCH,
                new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> message) {
                long expired = System.currentTimeMillis()
                        - expiration_age;
                Pattern p = Pattern.compile(message.body());

                JsonObject results = new JsonObject();

                Iterator<Entry<String, Long>> it = handlers.entrySet()
                        .iterator();

                while (it.hasNext()) {
                    Entry<String, Long> entry = it.next();
                    if (expiration_age > 0) {
                        if ((entry.getValue() == null)
                                || (entry.getValue().longValue() < expired)) {
                            continue;
                        }
                    }
                    if (p.matcher(entry.getKey()).matches()) {
                        results.putNumber(entry.getKey(), entry
                                .getValue().longValue());
                    }
                }

                message.reply(results);
            }
        });

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
                            it.remove();
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
      vertx.eventBus().publish(EVENTBUS_REGISTRY_EXPIRED, address);
    }


    private void maybeRegisterMBean() {
      MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
      try {
        mbs.registerMBean(this, new ObjectName("org.usergrid.vx.eventbus_registry:type=Registry"));
      } catch(InstanceAlreadyExistsException iaee) {
        log.info("Registry has already been registered with JMX");
      } catch(Exception ex) {
        log.error(ex);
      }
    }
}
