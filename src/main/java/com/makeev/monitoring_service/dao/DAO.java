package com.makeev.monitoring_service.dao;

import java.util.List;
import java.util.Optional;

/**
 * Interface for data access objects (DAO) providing common methods for accessing data.
 * @author Evgeniy Makeev
 * @version 1.4
 *
 * @param <T> The type of object handled by the DAO.
 */
public interface DAO<T> {

    /**
     * Retrieves an object by login.
     *
     * @param login The login identifier.
     * @return An optional containing the retrieved object, or empty if not found.
     */
    Optional<T> getByLogin(String login);

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
