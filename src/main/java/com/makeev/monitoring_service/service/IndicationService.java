package com.makeev.monitoring_service.service;

import com.makeev.monitoring_service.dao.CounterDAO;
import com.makeev.monitoring_service.dao.UserDAO;
import com.makeev.monitoring_service.exceptions.EmptyException;
import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.model.Indication;
import com.makeev.monitoring_service.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code IndicationService} class provides services related to user indications and counters.
 * It interacts with the {@link UserDAO} and {@link CounterDAO} to perform operations such as adding users,
 * retrieving indications, and handling exceptions.
 */
public class IndicationService {

    /**
     * The UserDAO instance for managing user data.
     */
    public final UserDAO userDAO = new UserDAO();

    /**
     * The CounterDAO instance for managing counter data.
     */
    public final CounterDAO counterDAO = new CounterDAO();

    /**
     * Adds a new user with the specified login and password.
     *
     * @param login    The login of the user.
     * @param password The password of the user.
     */
    public void addUser(String login, String password) {
        userDAO.add(new User(login, password, new HashMap<>(), false));
    }

    /**
     * Retrieves all indications for a user for a specific month.
     *
     * @param login The login of the user.
     * @param date  The date representing the month.
     * @return A map of counters to lists of indications for the specified user and month.
     * @throws EmptyException If no indications are found.
     */
    public Map<Counter, List<Indication>> getAllIndicationsForUserForMonth(String login, LocalDate date) throws EmptyException {
        Map<Counter, List<Indication>> mapOfIndicationForUser = userDAO.getAllIndicationsForUser(login);
        Map<Counter, List<Indication>> mapOfIndicationOfMonth = new HashMap<>();

        if (mapOfIndicationForUser.isEmpty()) {
            throw new EmptyException();
        } else {
            mapOfIndicationForUser.forEach((k, v) -> {
                for (Indication indication : v) {
                    if (indication.date().equals(date)) {
                        mapOfIndicationOfMonth.put(k, v);
                    }
                }
            });
            if (mapOfIndicationOfMonth.isEmpty()) {
                throw new EmptyException();
            }
        }
        return mapOfIndicationForUser;
    }

    /**
     * Retrieves the current indication for each counter for the specified user.
     *
     * @param login The login of the user.
     * @return A map of counters to the current indications for the specified user.
     * @throws EmptyException If no indications are found.
     */
    public Map<Counter, Indication> getCurrentIndication(String login) throws EmptyException {
        Map<Counter, List<Indication>> mapOfIndicationForUser = userDAO.getAllIndicationsForUser(login);
        Map<Counter, Indication> mapOfCurrentIndication = new HashMap<>();

        if (mapOfIndicationForUser.isEmpty()) {
            throw new EmptyException();
        } else {
            mapOfIndicationForUser.forEach((k, v) -> {
                int lastIndex = v.size() - 1;
                mapOfCurrentIndication.put(k, v.get(lastIndex));
            });
            if (mapOfCurrentIndication.isEmpty()) {
                throw new EmptyException();
            }
        }
        return mapOfCurrentIndication;
    }
}
