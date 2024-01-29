package com.makeev.monitoring_service.service;

import com.makeev.monitoring_service.constants.CounterOf;
import com.makeev.monitoring_service.model.User;

import java.util.Optional;

/**
 * An interface representing the service for managing user accounts and meter indications.
 *  @author Evgeniy Makeev
 *  @version 1.4
 */
public interface AccountService {

    /**
     * Adds a new user with the provided login and password.
     *
     * @param login The login of the new user.
     * @param pass The password of the new user.
     * @return true if the user is successfully added, false otherwise.
     */
    boolean addUser(String login, String pass);

    /**
     * Finds and checks a user based on the provided login and password.
     *
     * @param login The login of the user to be found.
     * @param pass The password of the user to be checked.
     * @return true if the user is found and the password matches, false otherwise.
     */
    boolean findAndCheckUser(String login, String pass);

    /**
     * Retrieves a user based on the provided login.
     *
     * @param login The login of the user to be retrieved.
     * @return An Optional containing the user if found, otherwise an empty Optional.
     */
    User getUserByLogin(Optional<String> login);

    /**
     * Adds an indication of user for a specific year, month, and counter type with the provided value.
     *
     * @param login The login of the user.
     * @param year The year for which the indication is added.
     * @param month The month for which the indication is added.
     * @param counterOf The type of counter for the indication.
     * @param value The value of the indication.
     * @return An indication ID if successfully added, -1 otherwise.
     */
    int addIndicationOfUser(String login, Integer year, Integer month, CounterOf counterOf, Double value);

    /**
     * Retrieves all indications for a specific user.
     *
     * @param login The login of the user.
     * @return A String representation of all indications for the user.
     */
    String getAllIndicationsForUser(String login);


    /**
     * Retrieves all indications for a specific user for a given year and month.
     *
     * @param login The login of the user.
     * @param year The year for which indications are retrieved.
     * @param month The month for which indications are retrieved.
     * @return A String representation of all indications for the user in the specified month and year.
     */
    String getAllIndicationsForUserForMonth(String login, Integer year, Integer month);

    /**
     * Retrieves the current meter value for a specific user and counter type.
     *
     * @param login The login of the user.
     * @param counterOf The type of counter for which the current meter is retrieved.
     * @return A String representation of the current meter value.
     */
    String getCurrentMeter(String login, CounterOf counterOf);

    /**
     * Checks if a user has administrator privileges.
     *
     * @param login The login of the user.
     * @return true if the user is an administrator, false otherwise.
     */
    boolean isAdmin(String login);

    /**
     * Checks if a user with the provided login exists.
     *
     * @param login The login of the user to be checked.
     * @return true if the user exists, false otherwise.
     */
    boolean findUser(String login);

    /**
     * Retrieves a String representation of all indications in the system.
     *
     * @return A String representation of all indications.
     */
    String getAllIndications();
}
