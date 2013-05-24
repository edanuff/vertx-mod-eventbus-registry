package org.usergrid.vx.eventbus_registry;

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

public class Registry extends Verticle {

    private static final Logger log = LoggerFactory.getLogger(Registry.class);

    public static final long DEFAULT_EXPIRATION_AGE = 5000;
    public static final long DEFAULT_PING_TIME = 1000;
    public static final long DEFAULT_SWEEP_TIME = 0;

    Map<String, Long> handlers = new HashMap<String, Long>();

    long expiration_age = DEFAULT_EXPIRATION_AGE;
    long ping_time = DEFAULT_PING_TIME;
    long sweep_time = DEFAULT_SWEEP_TIME;

    @Override
    public void start() {
        log.info("EventBus registry started.");

        JsonObject config = container.config();
        expiration_age = config.getLong("expiration", DEFAULT_EXPIRATION_AGE);
        ping_time = config.getLong("ping", DEFAULT_PING_TIME);
        sweep_time = config.getLong("sweep", DEFAULT_SWEEP_TIME);

        vertx.eventBus().registerHandler("eventbus.registry.register",
                new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> message) {
                handlers.put(message.body(), System.currentTimeMillis());
                log.info("EventBus registered address: " + message.body());
            }
        });

        vertx.eventBus().registerHandler("eventbus.registry.get",
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

        vertx.eventBus().registerHandler("eventbus.registry.search",
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
                    vertx.eventBus().publish("eventbus.registry.ping",
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
                            .publish("eventbus.registry.expired",
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
}
