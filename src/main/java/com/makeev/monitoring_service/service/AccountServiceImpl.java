package com.makeev.monitoring_service.service;

import com.makeev.monitoring_service.constants.CounterOf;
import com.makeev.monitoring_service.dao.DAO;
import com.makeev.monitoring_service.dao.IndicationOfUserDAOImpl;
import com.makeev.monitoring_service.dao.UserDAOImpl;
import com.makeev.monitoring_service.model.Indication;
import com.makeev.monitoring_service.model.IndicationOfUser;
import com.makeev.monitoring_service.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//TODO separate methods for working with Indication into a separate com.makeev.service

/**
 * Implementation of the {@link AccountService} interface providing methods for managing user accounts
 * and meter indications.
 * @author Evgeniy Makeev
 * @version 1.4
 */
public class AccountServiceImpl implements AccountService{

    /**
     * DAO for managing Users.
     */
    private final DAO<User> userDAO = new UserDAOImpl();

    /**
     * DAO for managing IndicationOfUser.
     */
    private final DAO<IndicationOfUser> indicationDAO = new IndicationOfUserDAOImpl();

    /**
     * Adds a new user with the provided login and password.
     *
     * @param login The login of the new user.
     * @param pass The password of the new user.
     * @return true if the user is successfully added, false otherwise.
     */
    @Override
    public boolean addUser(String login, String pass) {
        for (User user : userDAO.getAll()) {
            if (user.login().equals(login)) {
                return false;
            }
        }
        userDAO.add(new User(login, pass, false));
        return true;
    }

    /**
     * Finds and checks a user based on the provided login and password.
     *
     * @param login The login of the user to be found.
     * @param pass The password of the user to be checked.
     * @return true if the user is found and the password matches, false otherwise.
     */
    @Override
    public boolean findAndCheckUser(String login, String pass) {
        for (User user : userDAO.getAll()) {
            if (user.login().equals(login) && user.password().equals(pass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a user by the provided login.
     *
     * @param login The login of the user to be retrieved.
     * @return The user with the specified login.
     * @throws IllegalArgumentException if the user with the specified login is not found.
     */
    @Override
    public User getUserByLogin(Optional<String> login) {
        return userDAO.getByLogin(login.orElseThrow()).orElseThrow();
    }

    /**
     * Adds an indication of a user for a specific month.
     *
     * @param login The login of the user.
     * @param year The year of the indication.
     * @param month The month of the indication.
     * @param counterOf The type of counter for the indication.
     * @param value The value of the indication.
     * @return 1 if the indication is added successfully, 0 otherwise.
     */
    @Override
    public int addIndicationOfUser(String login, Integer year, Integer month, CounterOf counterOf, Double value) {
        User user = userDAO.getByLogin(login).orElseThrow();
        LocalDate date = LocalDate.of(year, month, 1);
        Indication indication = new Indication(date, value);
        int answer = 0;
        if (indicationDAO.getByLogin(login).isEmpty()) {
            indicationDAO.add(new IndicationOfUser(user, counterOf, indication));
            answer = 1;
        }
        boolean bigger = false;
        for (IndicationOfUser indicationOfUser : indicationDAO.getAll()) {
            if (indicationOfUser.user().equals(user) && indicationOfUser.counterOf().equals(counterOf)) {
                if (indicationOfUser.indication().date().isBefore(date) &&
                        indicationOfUser.indication().value() <= value) {
                    bigger = true;
                }
            }
        }
        if (bigger) {
            indicationDAO.add(new IndicationOfUser(user, counterOf, indication));
            answer = 1;
        }
        return answer;
    }

    /**
     * Retrieves the submission history of indications for a specific user.
     *
     * @param login The login of the user.
     * @return A formatted string representing the submission history of indications for the user.
     */
    @Override
    public String getAllIndicationsForUser(String login) {
        StringBuilder result = new StringBuilder("Indications submission history of " + login + ":\n");
        List<IndicationOfUser> listForUser = indicationDAO.getAll()
                .stream()
                .filter(i -> i.user().login().equals(login))
                .toList();
        if (!listForUser.isEmpty()) {
            for (IndicationOfUser indicationOfUser : listForUser) {
                format(result, indicationOfUser);
            }
        } else {
            result.append("None.");
        }
        return result.toString();
    }

    /**
     * Retrieves the submission history of indications for a specific user and month.
     *
     * @param login The login of the user.
     * @param year The year of the indications.
     * @param month The month of the indications.
     * @return A formatted string representing the submission history of indications for the user and month.
     */
    @Override
    public String getAllIndicationsForUserForMonth(String login, Integer year, Integer month) {
        LocalDate date = LocalDate.of(year, month, 1);
        StringBuilder result = new StringBuilder("Indications submission history for " + date.getMonth() + " - " + date.getYear() + ":\n");
        List<IndicationOfUser> listForUser = indicationDAO.getAll()
                .stream()
                .filter(i -> i.user().login().equals(login))
                .filter(i -> i.indication().date().equals(date))
                .toList();
        if (!listForUser.isEmpty()) {
            for (IndicationOfUser indicationOfUser : listForUser) {
                format(result, indicationOfUser);
            }
        } else {
            result.append("None.");
        }
        return result.toString();
    }

    /**
     * Retrieves the current meter indications for a specific user and counter type.
     *
     * @param login The login of the user.
     * @param counterOf The type of counter for which the indications are retrieved.
     * @return A formatted string representing the current meter indications for the user and counter type.
     */
    @Override
    public String getCurrentMeter(String login, CounterOf counterOf) {
        StringBuilder result = new StringBuilder("Current meter indications:\n");
        List<IndicationOfUser> listForUser = indicationDAO.getAll()
                .stream()
                .filter(i -> i.user().login().equals(login))
                .filter(i -> i.counterOf().equals(counterOf))
                .toList();
        if (!listForUser.isEmpty()) {
            int lastIndex = listForUser.size();
            format(result,listForUser.get(lastIndex - 1));
        } else {
            result.append("None.");
        }
        return result.toString();
    }

    /**
     * Checks if a user has admin privileges.
     *
     * @param login The login of the user.
     * @return {@code true} if the user has admin privileges, {@code false} otherwise.
     */
    @Override
    public boolean isAdmin(String login) {
        return userDAO.getByLogin(login).orElseThrow().admin();
    }

    /**
     * Checks if a user with the specified login exists.
     *
     * @param login The login to check.
     * @return {@code true} if the user exists, {@code false} otherwise.
     */
    @Override
    public boolean findUser(String login) {
        return userDAO.getByLogin(login).isPresent();
    }

    /**
     * Retrieves the submission history of indications for all users.
     *
     * @return A formatted string representing the submission history of indications for all users.
     */
    @Override
    public String getAllIndications() {
        StringBuilder result = new StringBuilder("Indications submission history for all users:\n");
        if (!indicationDAO.getAll().isEmpty()) {
            for (IndicationOfUser indicationOfUser : indicationDAO.getAll()) {
                format(result, indicationOfUser);
            }
        } else {
            result.append("None.");
        }
        return result.toString();
    }

    /**
     * Formats a {@link IndicationOfUser} object into a string and appends it to the result.
     *
     * @param result     The StringBuilder to which the formatted string is appended.
     * @param indicationOfUser  The IndicationOfUser to be formatted.
     */
    private void format(StringBuilder result, IndicationOfUser indicationOfUser) {
        result.append(indicationOfUser.counterOf())
                .append(" | ")
                .append(indicationOfUser.indication().date().getYear())
                .append(" - ")
                .append(indicationOfUser.indication().date().getMonth())
                .append(" | ")
                .append(indicationOfUser.indication().value())
                .append("\n");
    }
}
