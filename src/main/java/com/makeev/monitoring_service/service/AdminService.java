package com.makeev.monitoring_service.service;

import com.makeev.monitoring_service.aop.annotations.Loggable;
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
            "INSERT INTO non_public.user_events (date, message) VALUES (?,?)";
    private final static String GET_ALL_SQL = "SELECT * FROM non_public.user_events";


    @Loggable
    public void addEvent(String message) {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(ADD_SQL)) {
            statement.setString(1, LocalDate.now().toString());
            statement.setString(2, message);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Loggable
    public List<UserEvent> getAllEvents() throws EmptyException {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_ALL_SQL)) {
            var result = statement.executeQuery();
            List<UserEvent> listOfUserEvents = new ArrayList<>();
            while (result.next()) {
                listOfUserEvents.add(
                        new UserEvent(result.getString("date"),
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
