package com.microservice.erp.webapp.config.jdbc;

import java.util.function.Consumer;

/**
 * Created by Rajib on 9/10/2022.
 */
public interface DataSources<Key, Value> {
    /*default Value read(Key key) {
        return null;
    }

    default Value[] readSync(int offset, int pageSize) {
        return null;
    }

    default void readAsync(int offset, int pageSize, Consumer<Value[]> consumer) {
        if(consumer != null) {
            consumer.accept((Object)null);
        }

    }

    default boolean containsKey(Key key) {
        return false;
    }

    default void put(Key key, Value value) {
    }

    default Value remove(Key key) {
        return null;
    }

    default Value replace(Key key, Value value) {
        return null;
    }

    default boolean contains(Value value) {
        return this.containsKey(Integer.valueOf(value.hashCode()));
    }

    default void add(Value value) {
        this.put(Integer.valueOf(value.hashCode()), value);
    }

    default void delete(Value value) {
        this.remove(Integer.valueOf(value.hashCode()));
    }

    default int size() {
        return 0;
    }

    default void clear() {
    }*/
}