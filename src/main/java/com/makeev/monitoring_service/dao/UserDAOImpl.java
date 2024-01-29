package com.makeev.monitoring_service.dao;

import com.makeev.monitoring_service.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of the Data Access Object (DAO) for managing {@link User} entities.
 * Provides methods to retrieve, store, and manipulate user data.
 * @author Evgeniy Makeev
 * @version 1.4
 */
public class UserDAOImpl implements DAO<User> {

    /**
     * The list of {@link User} instances stored in the data store.
     */
    private final List<User> users = new ArrayList<>();

    /**
     * Constructs a new instance of {@code UserDAOImpl} and initializes it with a default admin user.
     */
    public UserDAOImpl() {
        User admin = new User("admin", "admin", true);
        users.add(admin);
    }

    /**
     * Retrieves a {@link User} object by user login.
     *
     * @param login The login identifier of the user.
     * @return An optional containing the retrieved {@link User}, or empty if not found.
     */
    @Override
    public Optional<User> getByLogin(String login) {
        for (User user : users) {
            if (Objects.equals(user.login(), login)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieves a list of all stored {@link User} instances.
     *
     * @return The list of all stored {@link User} instances.
     */
    @Override
    public List<User> getAll() {
        return users;
    }

    /**
     * Adds a new {@link User} instance to the data store.
     *
     * @param user The {@link User} instance to add.
     */
    @Override
    public void add(User user) {
        users.add(user);
    }
}
