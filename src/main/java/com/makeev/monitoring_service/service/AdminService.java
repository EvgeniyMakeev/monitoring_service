package com.makeev.monitoring_service.service;

import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.exceptions.EmptyException;
import com.makeev.monitoring_service.model.UserEvent;
import com.makeev.monitoring_service.utils.ConnectionManager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages admin-related events.
 */
public class AdminService {

    private final ConnectionManager connectionManager;

    public AdminService(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    private final static String ADD_SQL =
            "INSERT INTO non_public.user_events (date, user_login, message) VALUES (?,?,?)";
    private final static String GET_ALL_SQL = "SELECT * FROM non_public.user_events";
    private final static String GET_BY_LOGIN_SQL = GET_ALL_SQL + " WHERE user_login=?";


    /**
     * Adds an event for a specific user with a corresponding message.
     *
     * @param login The user of login for whom the event is added.
     * @param message The message describing the event.
     */
    public void addEvent(String login, String message) {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(ADD_SQL)) {
            statement.setString(1, LocalDate.now().toString());
            statement.setString(2, login);
            statement.setString(3, message);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Retrieves the submission history of all events.
     *
     * @return A formatted string representing the submission history of all events.
     */
    public List<UserEvent> getAllEvents() throws EmptyException {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_ALL_SQL)) {
            var result = statement.executeQuery();
            List<UserEvent> listOfUserEvents = new ArrayList<>();
            while (result.next()) {
                listOfUserEvents.add(
                        new UserEvent(result.getString("date"),
                                result.getString("user_login"),
                                result.getString("message"))
                );
            }
            if (listOfUserEvents.isEmpty()) {
                throw new EmptyException();
            }
            return listOfUserEvents;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Retrieves the submission history of events for a specific user.
     *
     * @param login The login of the user.
     * @return A formatted string representing the submission history of events for the user.
     */
    public List<UserEvent> getAllEventsForUser(String login) throws EmptyException {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_BY_LOGIN_SQL)) {
            statement.setString(1, login);
            var result = statement.executeQuery();
            List<UserEvent> listOfUserEvents = new ArrayList<>();
            while (result.next()) {
                listOfUserEvents.add(
                        new UserEvent(result.getString("date"),
                                result.getString("user_login"),
                                result.getString("message"))
                );
            }
            if (listOfUserEvents.isEmpty()) {
                throw new EmptyException();
            }
            return listOfUserEvents;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
