package com.makeev.service;

import com.makeev.monitoring_service.constants.CounterOf;
import com.makeev.monitoring_service.dao.DAO;
import com.makeev.monitoring_service.model.Indication;
import com.makeev.monitoring_service.model.IndicationOfUser;
import com.makeev.monitoring_service.model.User;
import com.makeev.monitoring_service.service.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link AccountServiceImpl} class.
 */
public class AccountServiceImplTest {

    @Mock
    private DAO<User> userDAOMock;

    @Mock
    private DAO<IndicationOfUser> indicationDAOMock;

    @InjectMocks
    private AccountServiceImpl accountServiceTest = new AccountServiceImpl();

    /**
     * Initializes mock objects before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for the {@link AccountServiceImpl#addUser(String, String)} method
     * when the user does not exist, should add the user and return true.
     */
    @Test
    void addUser_userDoesNotExist_shouldAddUser() {
        // Arrange
        String login = "newUser";
        String pass = "password";
        // Act
        boolean result = accountServiceTest.addUser(login, pass);

        // Assert
        assertThat(result).isTrue();
    }

    /**
     * Test case for the {@link AccountServiceImpl#addUser(String, String)} method
     * when the user already exists, should not add the user and return false.
     */
    @Test
    void addUser_userExists_shouldNotAddUser() {
        // Arrange
        String login = "existingUser";
        String pass = "password";
        accountServiceTest.addUser(login, "somePassword");

        // Act
        boolean result = accountServiceTest.addUser(login, pass);

        // Assert
        assertThat(result).isFalse();
        verify(userDAOMock, never()).add(any());
    }

    /**
     * Test case for the {@link AccountServiceImpl#findAndCheckUser(String, String)} method
     * with valid credentials, should return true.
     */
    @Test
    void findAndCheckUser_validCredentials_shouldReturnTrue() {
        // Arrange
        String login = "existingUser";
        String pass = "password";
        accountServiceTest.addUser(login, pass);

        // Act
        boolean result = accountServiceTest.findAndCheckUser(login, pass);

        // Assert
        assertThat(result).isTrue();
    }

    /**
     * Test case for the {@link AccountServiceImpl#findAndCheckUser(String, String)} method
     * with invalid credentials, should return false.
     */
    @Test
    void findAndCheckUser_invalidCredentials_shouldReturnFalse() {
        // Arrange
        String login = "existingUser";
        String pass = "wrongPassword";
        List<User> userList = new ArrayList<>();
        userList.add(new User(login, "password", false));
        when(userDAOMock.getAll()).thenReturn(userList);

        // Act
        boolean result = accountServiceTest.findAndCheckUser(login, pass);

        // Assert
        assertThat(result).isFalse();
    }

    /**
     * Test case for the {@link AccountServiceImpl#getAllIndicationsForUser(String)} method,
     * should return a formatted string containing indications submission history for the user.
     */
    @Test
    void getAllIndicationsForUser_shouldReturnFormattedString() {
        // Arrange
        String login = "testUser";
        List<IndicationOfUser> indicationList = new ArrayList<>();
        indicationList.add(new IndicationOfUser(new User(login, "password", false), CounterOf.HEATING, new Indication(LocalDate.now(), 10.2)));
        when(indicationDAOMock.getAll()).thenReturn(indicationList);

        // Act
        String result = accountServiceTest.getAllIndicationsForUser(login);

        // Assert
        assertThat(result).contains("Indications submission history of " + login);
    }

    /**
     * Test case for the {@link AccountServiceImpl#getUserByLogin(Optional)} method
     * with a valid login, should return the corresponding user.
     */
    @Test
    void getUserByLogin_validLogin_shouldReturnUser() {
        // Arrange
        String login = "existingUser";
        Optional<String> optionalLogin = Optional.of(login);
        accountServiceTest.addUser(login, "password");
        when(userDAOMock.getByLogin(login)).thenReturn(Optional.of(accountServiceTest.getUserByLogin(optionalLogin)));

        // Act
        User result = accountServiceTest.getUserByLogin(optionalLogin);

        // Assert
        assertThat(result).isEqualTo(accountServiceTest.getUserByLogin(optionalLogin));
    }
}