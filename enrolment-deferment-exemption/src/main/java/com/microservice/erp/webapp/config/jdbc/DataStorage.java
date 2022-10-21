package com.microservice.erp.webapp.config.jdbc;

/**
 * Created by Administrator on 9/10/2022.
 */
public interface DataStorage {
    default String getUuid() {
        return null;
    }

    default void save(boolean async) {
    }

    default boolean retrieve() {
        return false;
    }

    default boolean delete() {
        return false;
    }
}
