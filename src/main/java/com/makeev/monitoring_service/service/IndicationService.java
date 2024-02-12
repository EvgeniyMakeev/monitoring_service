package com.makeev.monitoring_service.service;

import com.makeev.monitoring_service.aop.annotations.Loggable;
import com.makeev.monitoring_service.dao.CounterDAO;
import com.makeev.monitoring_service.dao.UserDAO;
import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.exceptions.EmptyException;
import com.makeev.monitoring_service.exceptions.IncorrectValuesException;
import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.model.Indication;
import com.makeev.monitoring_service.model.IndicationsOfUser;
import com.makeev.monitoring_service.utils.ConnectionManager;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code IndicationService} class provides services related to user indications and counters.
 * It interacts with the {@link UserDAO} and {@link CounterDAO} to perform operations such as adding users,
 * retrieving indications, and handling exceptions.
 */
public class IndicationService {

    private final ConnectionManager connectionManager;

    public IndicationService(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * The CounterDAO instance for managing counter data.
     */
    public final CounterDAO counterDAO = new CounterDAO(new ConnectionManagerImpl());

    private final static String ADD_INDICATION_SQL ="""
                        INSERT INTO non_public.indications (user_login, counter_id, year, month, value)
                        VALUES (?,?,?,?,?)
                        """;
    private final static String GET_ALL_INDICATIONS_SQL =
            "SELECT * FROM non_public.indications";
    private final static String GET_ALL_INDICATIONS_FOR_USER_SQL =
            GET_ALL_INDICATIONS_SQL + " WHERE user_login=?";

    private final static String GET_ALL_INDICATIONS_FOR_USER_ON_DATE_SQL =
            GET_ALL_INDICATIONS_FOR_USER_SQL + " AND year=? AND month=?";
    private final static String GET_CURRENT_INDICATIONS_FOR_USER_SQL =
            GET_ALL_INDICATIONS_FOR_USER_SQL + """
               AND counter_id=?
              ORDER BY value DESC, month DESC
              LIMIT 1
            """;

    /**
     * Adds an Indication for a specific Counter and User.
     *
     * @param login  The login of the user.
     * @param counter The Counter for which the indication is submitted.
     * @param date    The date of the indication.
     * @param value   The value of the indication.
     * @throws IncorrectValuesException If the values are incorrect.
     */
    @Loggable
    public void addIndicationOfUser(String login, Counter counter, LocalDate date, Double value)
            throws IncorrectValuesException {
        try (var connection = connectionManager.open();
             var statementAdd = connection.prepareStatement(ADD_INDICATION_SQL)) {
            List<IndicationsOfUser> listOfIndicationsOfUser = new ArrayList<>();
            try {
                listOfIndicationsOfUser = getCurrentIndication(login);
            } catch (EmptyException e) {
                statementAdd.setString(1, login);
                statementAdd.setLong(2, counter.id());
                statementAdd.setInt(3, date.getYear());
                statementAdd.setInt(4, date.getMonthValue());
                statementAdd.setDouble(5, value);

                statementAdd.executeUpdate();
            }
            if (!listOfIndicationsOfUser.isEmpty()) {
                for (IndicationsOfUser indicationsOfUser : listOfIndicationsOfUser) {
                    if (indicationsOfUser.counter().equals(counter)
                            && (indicationsOfUser.indication().date().isAfter(date)
                            || indicationsOfUser.indication().value() > value)) {
                        throw new IncorrectValuesException();
                    }
                }
                statementAdd.setString(1, login);
                statementAdd.setLong(2, counter.id());
                statementAdd.setInt(3, date.getYear());
                statementAdd.setInt(4, date.getMonthValue());
                statementAdd.setDouble(5, value);

                statementAdd.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Retrieves all indications for a user.
     *
     * @return A map of Counters to a list of Indications for the specified user.
     * @throws EmptyException If no indications are found for the user.
     */
    @Loggable
    public List<IndicationsOfUser> getAllIndications() throws EmptyException {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_ALL_INDICATIONS_SQL)) {
            var result = statement.executeQuery();
            List<IndicationsOfUser> listOfIndicationsOfUser = new ArrayList<>();
            while (result.next()) {
                String login = result.getString("user_login");
                Long user_id = result.getLong("counter_id");
                int year = result.getInt("year");
                int month = result.getInt("month");
                LocalDate date = LocalDate.of(year, month, 1);
                Double value = result.getDouble("value");
                Indication indication = new Indication(date, value);
                listOfIndicationsOfUser.add(
                        new IndicationsOfUser(login,
                                counterDAO.getCounterById(user_id).orElseThrow(),
                                indication));
            }
            if (listOfIndicationsOfUser.isEmpty()) {
                throw new EmptyException();
            }
            return listOfIndicationsOfUser;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Loggable
    public List<IndicationsOfUser> getAllIndicationsForUser(String login) throws EmptyException {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_ALL_INDICATIONS_FOR_USER_SQL)) {
            statement.setString(1, login);
            var result = statement.executeQuery();
            List<IndicationsOfUser> listOfIndicationsOfUser = new ArrayList<>();
            while (result.next()) {
                Long user_id = result.getLong("counter_id");
                int year = result.getInt("year");
                int month = result.getInt("month");
                LocalDate date = LocalDate.of(year, month, 1);
                Double value = result.getDouble("value");
                listOfIndicationsOfUser.add(
                        new IndicationsOfUser(login,
                                counterDAO.getCounterById(user_id).orElseThrow(),
                                new Indication(date, value)));
            }
            if (listOfIndicationsOfUser.isEmpty()) {
                throw new EmptyException();
            }
            return listOfIndicationsOfUser;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Retrieves all indications for a user for a specific month.
     *
     * @param login The login of the user.
     * @param date  The date representing the month.
     * @return A map of counters to lists of indications for the specified user and month.
     * @throws EmptyException If no indications are found.
     */
    @Loggable
    public List<IndicationsOfUser> getAllIndicationsForUserForMonth(String login, LocalDate date) throws EmptyException {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_ALL_INDICATIONS_FOR_USER_ON_DATE_SQL)) {
            statement.setString(1, login);
            statement.setInt(2, date.getYear());
            statement.setInt(3, date.getMonthValue());
            var result = statement.executeQuery();
            List<IndicationsOfUser> listOfIndicationsOfUser = new ArrayList<>();
            while (result.next()) {
                Long user_id = result.getLong("counter_id");
                Double value = result.getDouble("value");
                listOfIndicationsOfUser.add(
                        new IndicationsOfUser(login,
                                counterDAO.getCounterById(user_id).orElseThrow(),
                                new Indication(date, value)));
            }
            if (listOfIndicationsOfUser.isEmpty()) {
                throw new EmptyException();
            }
            return listOfIndicationsOfUser;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Retrieves the current indication for each counter for the specified user.
     *
     * @param login The login of the user.
     * @return A map of counters to the current indications for the specified user.
     * @throws EmptyException If no indications are found.
     */
    @Loggable
    public List<IndicationsOfUser> getCurrentIndication(String login) throws EmptyException {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_CURRENT_INDICATIONS_FOR_USER_SQL)) {
            List<IndicationsOfUser> indicationsOfUserList = new ArrayList<>();
            List<Counter> listOfCounters = counterDAO.getAllCounters();
            for (Counter counter : listOfCounters) {
                statement.setString(1, login);
                statement.setLong(2, counter.id());

                var result = statement.executeQuery();
                while (result.next()) {
                    indicationsOfUserList.add(new IndicationsOfUser(login,
                            counter, new Indication(LocalDate.of(result.getInt("year"),
                            result.getInt("month"), 1),
                            result.getDouble("value"))));
                }
            }
            if (indicationsOfUserList.isEmpty()) {
                throw new EmptyException();
            }
            return indicationsOfUserList;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
