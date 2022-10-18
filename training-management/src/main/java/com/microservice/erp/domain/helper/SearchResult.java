package com.microservice.erp.domain.helper;

import java.util.List;

public interface SearchResult<T> {

    /**
     * @return total elements count
     */
    long getTotalCount();

    /**
     * @return total pages count
     */
    int getTotalPages();

    /**
     * @return page content as List. Never null.
     */
    List<T> getContent();
}
