package com.makeev.monitoring_service.dao;

import com.makeev.monitoring_service.model.IndicationOfUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of the Data Access Object (DAO) for managing {@link IndicationOfUser} entities.
 * Provides methods to retrieve, store, and manipulate indication data for users.
 * @author Evgeniy Makeev
 * @version 1.4
 */
public class IndicationOfUserDAOImpl implements DAO<IndicationOfUser> {

    /**
     * The list of {@link IndicationOfUser} instances stored in the data store.
     */
    private final List<IndicationOfUser> indicationOfUserList = new ArrayList<>();

    /**
     * Retrieves an {@link IndicationOfUser} object by user login.
     *
     * @param login The login identifier of the user.
     * @return An optional containing the retrieved {@link IndicationOfUser}, or empty if not found.
     */
    @Override
    public Optional<IndicationOfUser> getByLogin(String login) {
        for (IndicationOfUser indicationOfUser : indicationOfUserList) {
            if (Objects.equals(indicationOfUser.user().login(), login)) {
                return Optional.of(indicationOfUser);
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieves a list of all stored {@link IndicationOfUser} instances.
     *
     * @return The list of all stored {@link IndicationOfUser} instances.
     */
    @Override
    public List<IndicationOfUser> getAll() {
        return indicationOfUserList;
    }

    /**
     * Adds a new {@link IndicationOfUser} instance to the data store.
     *
     * @param indicationOfUser The {@link IndicationOfUser} instance to add.
     */
    public void add(IndicationOfUser indicationOfUser){
        indicationOfUserList.add(indicationOfUser);
    }
}
