package org.usergrid.vx.eventbus_registry;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

public class RegisteredEventBus implements EventBus {

    private final EventBus eventBus;

    public RegisteredEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void close(Handler<AsyncResult<Void>> doneHandler) {
        eventBus.close(doneHandler);
    }

    @Override
    public EventBus publish(String address, Object message) {
        return eventBus.publish(address, message);
    }

    @Override
    public EventBus publish(String address, JsonObject message) {
        return eventBus.publish(address, message);
    }

    @Override
    public EventBus publish(String address, JsonArray message) {
        return eventBus.publish(address, message);
    }

    @Override
    public EventBus publish(String address, Buffer message) {
        return eventBus.publish(address, message);
    }

    @Override
    public EventBus publish(String address, byte[] message) {
        return eventBus.publish(address, message);
    }

    @Override
    public EventBus publish(String address, String message) {
        return eventBus.publish(address, message);
    }

    @Override
    public EventBus publish(String address, Integer message) {
        return eventBus.publish(address, message);
    }

    @Override
    public EventBus publish(String address, Long message) {
        return eventBus.publish(address, message);
    }

    @Override
    public EventBus publish(String address, Float message) {
        return eventBus.publish(address, message);
    }

    @Override
    public EventBus publish(String address, Double message) {
        return eventBus.publish(address, message);
    }

    @Override
    public EventBus publish(String address, Boolean message) {
        return eventBus.publish(address, message);
    }

    @Override
    public EventBus publish(String address, Short message) {
        return eventBus.publish(address, message);
    }

    @Override
    public EventBus publish(String address, Character message) {
        return eventBus.publish(address, message);
    }

    @Override
    public EventBus publish(String address, Byte message) {
        return eventBus.publish(address, message);
    }

    @Override
    public EventBus registerHandler(String address,
            @SuppressWarnings("rawtypes") Handler<? extends Message> handler) {
        eventBus.publish("eventbus.registry.register", address);
        return eventBus.registerHandler(address, handler);
    }

    @Override
    public EventBus registerHandler(String address,
            @SuppressWarnings("rawtypes") Handler<? extends Message> handler,
            Handler<AsyncResult<Void>> resultHandler) {
        eventBus.publish("eventbus.registry.register", address);
        return eventBus.registerHandler(address, handler, resultHandler);
    }

    @Override
    public EventBus registerLocalHandler(String address,
            @SuppressWarnings("rawtypes") Handler<? extends Message> handler) {
        eventBus.publish("eventbus.registry.register", address);
        return eventBus.registerHandler(address, handler);
    }

    @Override
    public EventBus send(String address, Object message) {
        return eventBus.send(address, message);
    }

    @Override
    public EventBus send(String address, JsonObject message) {
        return eventBus.send(address, message);
    }

    @Override
    public EventBus send(String address, JsonArray message) {
        return eventBus.send(address, message);
    }

    @Override
    public EventBus send(String address, Buffer message) {
        return eventBus.send(address, message);
    }

    @Override
    public EventBus send(String address, byte[] message) {
        return eventBus.send(address, message);
    }

    @Override
    public EventBus send(String address, String message) {
        return eventBus.send(address, message);
    }

    @Override
    public EventBus send(String address, Integer message) {
        return eventBus.send(address, message);
    }

    @Override
    public EventBus send(String address, Long message) {
        return eventBus.send(address, message);
    }

    @Override
    public EventBus send(String address, Float message) {
        return eventBus.send(address, message);
    }

    @Override
    public EventBus send(String address, Double message) {
        return eventBus.send(address, message);
    }

    @Override
    public EventBus send(String address, Boolean message) {
        return eventBus.send(address, message);
    }

    @Override
    public EventBus send(String address, Short message) {
        return eventBus.send(address, message);
    }

    @Override
    public EventBus send(String address, Character message) {
        return eventBus.send(address, message);
    }

    @Override
    public EventBus send(String address, Byte message) {
        return eventBus.send(address, message);
    }

    @Override
    public EventBus send(String address, Object message,
            @SuppressWarnings("rawtypes") Handler<Message> replyHandler) {
        return eventBus.send(address, message, replyHandler);
    }

    @Override
    public <T> EventBus send(String address, JsonObject message,
            Handler<Message<T>> replyHandler) {
        return eventBus.send(address, message, replyHandler);
    }

    @Override
    public <T> EventBus send(String address, JsonArray message,
            Handler<Message<T>> replyHandler) {
        return eventBus.send(address, message, replyHandler);
    }

    @Override
    public <T> EventBus send(String address, Buffer message,
            Handler<Message<T>> replyHandler) {
        return eventBus.send(address, message, replyHandler);
    }

    @Override
    public <T> EventBus send(String address, byte[] message,
            Handler<Message<T>> replyHandler) {
        return eventBus.send(address, message, replyHandler);
    }

    @Override
    public <T> EventBus send(String address, String message,
            Handler<Message<T>> replyHandler) {
        return eventBus.send(address, message, replyHandler);
    }

    @Override
    public <T> EventBus send(String address, Integer message,
            Handler<Message<T>> replyHandler) {
        return eventBus.send(address, message, replyHandler);
    }

    @Override
    public <T> EventBus send(String address, Long message,
            Handler<Message<T>> replyHandler) {
        return eventBus.send(address, message, replyHandler);
    }

    @Override
    public <T> EventBus send(String address, Float message,
            Handler<Message<T>> replyHandler) {
        return eventBus.send(address, message, replyHandler);
    }

    @Override
    public <T> EventBus send(String address, Double message,
            Handler<Message<T>> replyHandler) {
        return eventBus.send(address, message, replyHandler);
    }

    @Override
    public <T> EventBus send(String address, Boolean message,
            Handler<Message<T>> replyHandler) {
        return eventBus.send(address, message, replyHandler);
    }

    @Override
    public <T> EventBus send(String address, Short message,
            Handler<Message<T>> replyHandler) {
        return eventBus.send(address, message, replyHandler);
    }

    @Override
    public <T> EventBus send(String address, Character message,
            Handler<Message<T>> replyHandler) {
        return eventBus.send(address, message, replyHandler);
    }

    @Override
    public <T> EventBus send(String address, Byte message,
            Handler<Message<T>> replyHandler) {
        return eventBus.send(address, message, replyHandler);
    }

    @Override
    public EventBus unregisterHandler(String address,
            @SuppressWarnings("rawtypes") Handler<? extends Message> handler) {
        return eventBus.unregisterHandler(address, handler);
    }

    @Override
    public EventBus unregisterHandler(String address,
            @SuppressWarnings("rawtypes") Handler<? extends Message> handler,
            Handler<AsyncResult<Void>> replyHandler) {
        return eventBus.unregisterHandler(address, handler, replyHandler);
    }

}
