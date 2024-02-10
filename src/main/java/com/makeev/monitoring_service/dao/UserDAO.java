package com.makeev.monitoring_service.dao;

import com.makeev.monitoring_service.aop.annotations.Loggable;
import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.exceptions.LoginAlreadyExistsException;
import com.makeev.monitoring_service.exceptions.VerificationException;
import com.makeev.monitoring_service.model.User;
import com.makeev.monitoring_service.utils.ConnectionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The {@code UserDAO} class is responsible for managing the persistence of User entities.
 * It provides methods to retrieve, add, and query User information, as well as handle
 * user-related operations such as login verification and indication submission.
 */
@Loggable
public class UserDAO implements DAO<User, String> {

    private final ConnectionManager connectionManager;

    public UserDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    private final static String ADD_SQL =
            "INSERT INTO non_public.users (login, password) VALUES (?,?)";
    private final static String GET_ALL_SQL = "SELECT * FROM non_public.users";
    private final static String GET_BY_LOGIN_SQL = GET_ALL_SQL + " WHERE login=?";
    private final static String GET_PASSWORD_SQL = "SELECT COUNT(*) FROM non_public.users WHERE login=? AND password=?";
    private final static String GET_ADMIN_SQL = "SELECT admin FROM non_public.users WHERE login=?";
    private final static String GET_LOGIN_SQL = "SELECT login FROM non_public.users WHERE login=?";

    /**
     * Adds a User entity to the map.
     *
     * @param user The User entity to add.
     */
    @Override
    public void add(User user) {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(ADD_SQL)) {
            statement.setString(1, user.login());
            statement.setString(2, user.password());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    /**
     * Retrieves a User entity by its login.
     *
     * @param login The login of the User to retrieve.
     * @return An {@code Optional} containing the User if found, or empty if not found.
     */
    @Override
    public Optional<User> getBy(String login) {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_BY_LOGIN_SQL)) {
            statement.setString(1, login);
            User user = null;
            var result = statement.executeQuery();
            if (result.next()) {
                user = new User(login,
                        result.getString("password"),
                        result.getBoolean("admin"));
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Retrieves a list of all User entities.
     *
     * @return The list of all User entities.
     */
    @Override
    public List<User> getAll() {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_ALL_SQL)) {
            List<User> listOfUsers = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next()) {
                listOfUsers.add(
                        new User(result.getString("login"),
                                result.getString("password"),
                                result.getBoolean("admin"))
                );
            }
            return listOfUsers;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Checks if a login already exists in the map.
     *
     * @param login The login to check for existence.
     * @throws LoginAlreadyExistsException If the login already exists.
     */
    public void existByLogin(String login) throws LoginAlreadyExistsException {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_LOGIN_SQL)) {
            statement.setString(1, login);
            var result = statement.executeQuery();
            if (result.next()) {
                throw new LoginAlreadyExistsException();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
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
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_PASSWORD_SQL)) {
            statement.setString(1, login);
            statement.setString(2, password);
            var result = statement.executeQuery();
            if (result.next()) {
                if (result.getInt(1) == 0) {
                    throw new VerificationException();
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Checks if a user is an admin.
     *
     * @param login The login of the user.
     * @return {@code true} if the user is an admin, {@code false} otherwise.
     */
    public boolean isAdmin(String login) {
        try (var connection = connectionManager.open();
             var statement = connection.prepareStatement(GET_ADMIN_SQL)) {
            statement.setString(1, login);
            var result = statement.executeQuery();
            if (result.next()) {
                return result.getBoolean("admin");
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
