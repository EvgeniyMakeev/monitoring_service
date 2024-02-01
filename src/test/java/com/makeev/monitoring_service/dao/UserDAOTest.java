package com.makeev.monitoring_service.dao;

import com.makeev.monitoring_service.exceptions.EmptyException;
import com.makeev.monitoring_service.exceptions.IncorrectValuesException;
import com.makeev.monitoring_service.exceptions.LoginAlreadyExistsException;
import com.makeev.monitoring_service.exceptions.VerificationException;
import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.model.Indication;
import com.makeev.monitoring_service.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Unit tests for {@link UserDAO}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserDAO Test")
class UserDAOTest {

    @Mock
    private Counter counter;

    @InjectMocks
    private UserDAO userDAO;

    @Test
    @DisplayName("Get User by Login - Success")
    void testGetUserByLogin_Success() {
        // Given
        String loginToFind = "admin";
        User admin = new User(loginToFind, "admin", new HashMap<>(), true);
        userDAO.add(admin);

        // When
        Optional<User> result = userDAO.getBy(loginToFind);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(admin);
    }

    @Test
    @DisplayName("Get User by Login - User not found")
    void testGetUserByLogin_UserNotFound() {
        // Given
        String loginToFind = "nonexistent";

        // When
        Optional<User> result = userDAO.getBy(loginToFind);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Get All Users - Success")
    void testGetAllUsers_Success() {
        // Given
        User admin = new User("admin", "admin", new HashMap<>(), true);
        User user = new User("user", "user", new HashMap<>(), false);
        userDAO.add(admin);
        userDAO.add(user);

        // When
        List<User> result = userDAO.getAll();

        // Then
        assertThat(result).containsExactlyInAnyOrder(admin, user);
    }

    @Test
    @DisplayName("Add User - Success")
    void testAddUser_Success() {
        // Given
        User newUser = new User("newUser", "password", new HashMap<>(), false);

        // When
        userDAO.add(newUser);

        // Then
        assertThat(userDAO.getBy("newUser")).isPresent();
    }

    @Test
    @DisplayName("Check if Login Already Exists - Login already exists")
    void testExistByLogin_LoginAlreadyExists() {
        // Given
        User existingUser = new User("existingUser", "password", new HashMap<>(), false);
        userDAO.add(existingUser);

        // When
        assertThatExceptionOfType(LoginAlreadyExistsException.class)
                .isThrownBy(() -> userDAO.existByLogin("existingUser"))
                .withMessage("A user with this login already exists!");
    }

    @Test
    @DisplayName("Check Credentials - Wrong Login")
    void testCheckCredentials_WrongLogin() {
        // Given
        User existingUser = new User("existent", "password", new HashMap<>(), false);
        userDAO.add(existingUser);

        // When
        assertThatExceptionOfType(VerificationException.class)
                .isThrownBy(() -> userDAO.checkCredentials("nonexistent", "password"))
                .withMessage("Access is denied!\nWrong login or password.");
    }

    @Test
    @DisplayName("Check Credentials - Wrong Password")
    void testCheckCredentials_WrongPassword() {
        // Given
        User user = new User("user", "password", new HashMap<>(), false);
        userDAO.add(user);

        // When
        assertThatExceptionOfType(VerificationException.class)
                .isThrownBy(() -> userDAO.checkCredentials("user", "wrongPassword"))
                .withMessage("Access is denied!\nWrong login or password.");
    }

    @Test
    @DisplayName("Check Credentials - User not found")
    void testCheckCredentials_UserNotFound() {
        // When
        assertThatExceptionOfType(VerificationException.class)
                .isThrownBy(() -> userDAO.checkCredentials("nonexistent", "password"))
                .withMessage("Access is denied!\nWrong login or password.");
    }

    @Test
    @DisplayName("Check if User is Admin - Admin User")
    void testIsAdmin_AdminUser() {
        // Given
        User admin = new User("admin", "admin", new HashMap<>(), true);
        userDAO.add(admin);

        // When
        boolean result = userDAO.isAdmin("admin");

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Check if User is Admin - Regular User")
    void testIsAdmin_RegularUser() {
        // Given
        User user = new User("user", "password", new HashMap<>(), false);
        userDAO.add(user);

        // When
        boolean result = userDAO.isAdmin("user");

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Add Indication for User - Success")
    void testAddIndicationOfUser_Success() throws IncorrectValuesException, EmptyException {
        // Given
        User user = new User("user", "password", new HashMap<>(), false);
        userDAO.add(user);


        // When
        userDAO.addIndicationOfUser("user", counter, LocalDate.now(), 50.0);

        // Then
        assertThat(userDAO.getAllIndicationsForUser("user")).isNotEmpty();
    }

    @Test
    @DisplayName("Add Indication for User - Incorrect Values")
    void testAddIndicationOfUser_IncorrectValues() throws IncorrectValuesException {
        // Given
        User user = new User("user", "password", new HashMap<>(), false);
        userDAO.add(user);

        userDAO.addIndicationOfUser("user", counter, LocalDate.now(), 60.0);

        // When
        assertThatExceptionOfType(IncorrectValuesException.class)
                .isThrownBy(() -> userDAO.addIndicationOfUser("user", counter, LocalDate.now(), 40.0))
                .withMessage("Meter readings have not been sent.\n" +
                        "Testimony can be submitted only once a month\n" +
                        "and it cannot be less than the current one.");
    }

    @Test
    @DisplayName("Get All Indications for User - Success")
    void testGetAllIndicationsForUser_Success() throws EmptyException, IncorrectValuesException {
        // Given
        User user = new User("user", "password", new HashMap<>(), false);
        userDAO.add(user);

        userDAO.addIndicationOfUser("user", counter, LocalDate.now(), 50.0);

        // When
        Map<Counter, List<Indication>> result = userDAO.getAllIndicationsForUser("user");

        // Then
        assertThat(result).isNotEmpty();
    }

}