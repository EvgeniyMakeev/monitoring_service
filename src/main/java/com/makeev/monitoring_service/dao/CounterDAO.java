package com.makeev.monitoring_service.dao;

import com.makeev.monitoring_service.aop.annotations.Loggable;
import com.makeev.monitoring_service.aop.annotations.LoggableEvent;
import com.makeev.monitoring_service.exceptions.CounterAlreadyExistsException;
import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.utils.ConnectionManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The {@code CounterDAO} class is responsible for managing the persistence
 * of Counter entities. It provides methods to retrieve, add, and query Counters.
 */
public class CounterDAO {

    private final ConnectionManager connectionManager;

    public CounterDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    private final static String ADD_SQL =
            "INSERT INTO non_public.counters (name) VALUES (?)";
    private final static String GET_ALL_SQL = "SELECT * FROM non_public.counters";
    private final static String GET_BY_ID_SQL = GET_ALL_SQL + " WHERE id=?";
    private final static String CHECK_NAME_OF_COUNTER_SQL = "SELECT name FROM non_public.counters WHERE name=?";

    @Loggable
    @LoggableEvent
    public Counter add(String nameOfCounter) throws CounterAlreadyExistsException {
        try (var connection = connectionManager.open();
             var statementCheck = connection.prepareStatement(CHECK_NAME_OF_COUNTER_SQL);
             var statementAdd = connection.prepareStatement(ADD_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statementCheck.setString(1, nameOfCounter);
            var result = statementCheck.executeQuery();
            if (result.next()) {
                throw new CounterAlreadyExistsException();
            }
            statementAdd.setString(1, nameOfCounter);
            statementAdd.executeUpdate();
            var key = statementAdd.getGeneratedKeys();
            Long id = -1L;
            if (key.next()){
                id = key.getLong("id");
            }
            return new Counter(id, nameOfCounter);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Retrieves a Counter entity by its name.
     *
     * @param id The id of the Counter to retrieve.
     * @return An {@code Optional} containing the Counter if found, or empty if not found.
     */
    @Loggable
    @LoggableEvent
    public Optional<Counter> getBy(Long id) {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_BY_ID_SQL)) {
            statement.setLong(1, id);
            Counter counter = null;
            var result = statement.executeQuery();
            if (result.next()) {
                counter = new Counter(id,result.getString("name"));
            }
            return Optional.ofNullable(counter);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Retrieves a list of all Counter entities.
     *
     * @return The list of all Counter entities.
     */
    @Loggable
    @LoggableEvent
    public List<Counter> getAll() {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_ALL_SQL)) {
            List<Counter> listOfCounters = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next()) {
                listOfCounters.add(
                        new Counter(result.getLong("id"),
                                result.getString("name"))
                );
            }
            return listOfCounters;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
