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
 * The {@code UserDAO} class is responsible for managing the persistence of User entities.
 * It provides methods to retrieve, add, and query User information, as well as handle
 * user-related operations such as login verification and indication submission.
 */
public class UserDAO implements DAO<User, String> {

    /**
     * A map storing User entities with login as the key.
     */
    private final Map<String, User> mapOfUser = new HashMap<>();

    /**
     * Default constructor that initializes the UserDAO with an admin user.
     */
    public UserDAO() {
        String login = "admin";
        User admin = new User(login, "admin", new HashMap<>(), true);
        mapOfUser.put(login, admin);
    }

    /**
     * Retrieves a User entity by its login.
     *
     * @param login The login of the User to retrieve.
     * @return An {@code Optional} containing the User if found, or empty if not found.
     */
    @Override
    public Optional<User> getBy(String login) {
        return Optional.ofNullable(mapOfUser.get(login));
    }

    /**
     * Retrieves a list of all User entities.
     *
     * @return The list of all User entities.
     */
    @Override
    public List<User> getAll() {
        return new ArrayList<>(mapOfUser.values());
    }

    /**
     * Adds a User entity to the map.
     *
     * @param user The User entity to add.
     */
    @Override
    public void add(User user) {
        mapOfUser.put(user.login(), user);
    }

    /**
     * Checks if a login already exists in the map.
     *
     * @param login The login to check for existence.
     * @throws LoginAlreadyExistsException If the login already exists.
     */
    public void existByLogin(String login) throws LoginAlreadyExistsException {
        if (mapOfUser.containsKey(login)) {
            throw new LoginAlreadyExistsException();
        }
    }

    /**
     * Verifies the credentials of a user.
     *
     * @param login    The login to verify.
     * @param password The password to verify.
     * @throws VerificationException If the verification fails.
     */
    public void checkCredentials(String login, String password) throws VerificationException {
        if (!mapOfUser.containsKey(login) || !mapOfUser.get(login).password().equals(password)) {
            throw new VerificationException();
        }
    }

    /**
     * Checks if a user is an admin.
     *
     * @param login The login of the user.
     * @return {@code true} if the user is an admin, {@code false} otherwise.
     */
    public boolean isAdmin(String login) {
        return mapOfUser.get(login).admin();
    }

    /**
     * Adds an Indication for a specific Counter and User.
     *
     * @param login  The login of the user.
     * @param counter The Counter for which the indication is submitted.
     * @param date    The date of the indication.
     * @param value   The value of the indication.
     * @throws IncorrectValuesException If the values are incorrect.
     */
    public void addIndicationOfUser(String login, Counter counter, LocalDate date, Double value)
            throws IncorrectValuesException {
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
        mapOfUser.get(login).mapOfIndicationOfCounter().put(counter, listOfIndications);
    }

    /**
     * Retrieves all indications for a user.
     *
     * @param login The login of the user.
     * @return A map of Counters to a list of Indications for the specified user.
     * @throws EmptyException If no indications are found for the user.
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
