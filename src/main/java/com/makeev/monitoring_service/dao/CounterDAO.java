package com.makeev.monitoring_service.dao;

import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.utils.ConnectionManager;

import java.sql.SQLException;
import java.util.*;

/**
 * The {@code CounterDAO} class is responsible for managing the persistence
 * of Counter entities. It provides methods to retrieve, add, and query Counters.
 */
public class CounterDAO implements DAO<Counter, Long> {

    private final static CounterDAO INSTANCE = new CounterDAO();

    public static CounterDAO getInstance() {
        return INSTANCE;
    }

    private final static String ADD_SQL =
            "INSERT INTO user_db.counters (name) VALUES (?)";
    private final static String GET_ALL_SQL = "SELECT * FROM user_db.counters";
    private final static String GET_BY_ID_SQL = GET_ALL_SQL + " WHERE id=?";
    private final static String COUNT_SQL = "SELECT COUNT(*) FROM user_db.counters";

    /**
     * Adds a Counter entity to the list.
     *
     * @param counter The Counter entity to add.
     */
    @Override
    public void add(Counter counter) {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(ADD_SQL)) {
            statement.setString(1, counter.name());
            statement.executeUpdate();
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
    @Override
    public Optional<Counter> getBy(Long id) {
        try (var connection = ConnectionManager.open();
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
    @Override
    public List<Counter> getAll() {
        try (var connection = ConnectionManager.open();
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

    /**
     * Gets the size of the list of Counter entities.
     *
     * @return The size of the list.
     */
    public int getNumberOfCounters() {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(COUNT_SQL)) {
            int quantityOfCounters = 0;
            var result = statement.executeQuery();
            if (result.next()) {
                quantityOfCounters = result.getInt(1);
            }
            return quantityOfCounters;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
