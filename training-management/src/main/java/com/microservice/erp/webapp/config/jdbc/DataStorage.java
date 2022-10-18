package com.microservice.erp.webapp.config.jdbc;

/**
 * @author Rajib Kumer Ghosh
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
