package com.makeev.monitoring_service.dao;

import com.makeev.monitoring_service.exceptions.EmptyException;
import com.makeev.monitoring_service.exceptions.IncorrectValuesException;
import com.makeev.monitoring_service.exceptions.LoginAlreadyExistsException;
import com.makeev.monitoring_service.exceptions.VerificationException;
import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.model.Indication;
import com.makeev.monitoring_service.model.User;

import java.time.LocalDate;
import java.util.*;

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
    private final Map<String, User> mapOfUser = new HashMap<>();

    /**
     * Constructs a new instance of {@code UserDAOImpl} and initializes it with a default admin user.
     */
    public UserDAOImpl() {
        String login = "admin";
        User admin = new User(login, "admin", new HashMap<>(), true);
        mapOfUser.put(login, admin);
    }

    /**
     * Retrieves a {@link User} object by user login.
     *
     * @param login The login identifier of the user.
     * @return An optional containing the retrieved {@link User}, or empty if not found.
     */
    @Override
    public Optional<User> getBy(String login) {
        return Optional.ofNullable(mapOfUser.get(login));
    }

    /**
     * Retrieves a list of all stored {@link User} instances.
     *
     * @return The list of all stored {@link User} instances.
     */
    @Override
    public List<User> getAll() {
        return new ArrayList<>(mapOfUser.values());
    }

    /**
     * Adds a new {@link User} instance to the data store.
     *
     * @param user The {@link User} instance to add.
     */
    @Override
    public void add(User user) {
        mapOfUser.put(user.login(), user);
    }

    /**
     * Adds a new user with the provided login and password.
     *
     * @param login The login of the new user.
     */
    public void existByLogin(String login) throws LoginAlreadyExistsException {
        if (mapOfUser.containsKey(login)) {
            throw new LoginAlreadyExistsException();
        }
    }
    /**
     * Finds and checks a user based on the provided login and password.
     *
     * @param login The login of the user to be found.
     * @param password The password of the user to be checked.
     */
    public void checkCredentials(String login, String password) throws VerificationException {
        if (!mapOfUser.containsKey(login)) {
            throw new VerificationException();
        }
        if (!mapOfUser.get(login).password().equals(password)) {
            throw new VerificationException();
        }
    }
    /**
     * Checks if a user has admin privileges.
     *
     * @param login The login of the user.
     * @return {@code true} if the user has admin privileges, {@code false} otherwise.
     */
    public boolean isAdmin(String login) {
        return mapOfUser.get(login).admin();
    }


    public void addIndicationOfUser(String login, Counter counter,LocalDate date,
                                    Double value) throws IncorrectValuesException {
        List<Indication> listOfIndications = new ArrayList<>();
        if (!mapOfUser.get(login).mapOfIndicationOfCounter().isEmpty()) {
            if (mapOfUser.get(login).mapOfIndicationOfCounter().containsKey(counter)) {
                listOfIndications = mapOfUser.get(login).mapOfIndicationOfCounter().get(counter);
            }
        }
        for (Indication indication : listOfIndications) {
            if (indication.date().isAfter(date) || indication.date().equals(date)
                        || indication.value() > value) {
                throw new IncorrectValuesException();
            }
        }
        listOfIndications.add(new Indication(date, value));
        mapOfUser.get(login).mapOfIndicationOfCounter()
                .put(counter, listOfIndications);
    }

    /**
     * Retrieves the submission history of indications for a specific user.
     *
     * @param login The login of the user.
     * @return A formatted string representing the submission history of indications for the user.
     */
    public Map<Counter, List<Indication>> getAllIndicationsForUser(String login) throws EmptyException {
        Map<Counter, List<Indication>> mapOfAllIndicationsForUser =
                mapOfUser.get(login).mapOfIndicationOfCounter();

        if (mapOfAllIndicationsForUser.isEmpty()) {
            throw new EmptyException();
        }
        return mapOfUser.get(login).mapOfIndicationOfCounter();
    }
}
