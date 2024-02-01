package com.makeev.monitoring_service.dao;

import java.util.List;
import java.util.Optional;

/**
 * Interface for data access objects (DAO) providing common methods for accessing data.
 * @param <T> The type of object handled by the DAO.
 */
public interface DAO<T, K> {

    /**
     * Retrieves an object by login.
     *
     * @param k Some identifier.
     * @return An optional containing the retrieved object, or empty if not found.
     */
    Optional<T> getBy(K k);

    /**
     * Retrieves a list of all objects.
     *
     * @return The list of all objects.
     */
    List<T> getAll();

    /**
     * Adds a new object to the data store.
     *
     * @param t The object to add.
     */
    void add(T t);
}
