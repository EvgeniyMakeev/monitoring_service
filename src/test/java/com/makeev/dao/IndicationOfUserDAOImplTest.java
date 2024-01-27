package com.makeev.dao;

import com.makeev.monitoring_service.constants.CounterOf;
import com.makeev.monitoring_service.dao.IndicationOfUserDAOImpl;
import com.makeev.monitoring_service.model.Indication;
import com.makeev.monitoring_service.model.IndicationOfUser;
import com.makeev.monitoring_service.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link IndicationOfUserDAOImpl} class.
 */
public class IndicationOfUserDAOImplTest {

    /**
     * The instance of the {@link IndicationOfUserDAOImpl} class to be tested.
     */
    private IndicationOfUserDAOImpl indicationOfUserDAO;

    /**
     * Setup method to initialize test resources before each test case.
     */
    @BeforeEach
    void setUp() {
        indicationOfUserDAO = Mockito.spy(new IndicationOfUserDAOImpl());
    }

    /**
     * Test case for the {@link IndicationOfUserDAOImpl#getByLogin(String)} method
     * with an existing IndicationOfUser, should return an Optional containing the IndicationOfUser.
     */
    @Test
    void getByLogin_existingIndicationOfUser_returnsOptionalIndicationOfUser() {
        // Arrange
        String login = "user1";
        IndicationOfUser indicationOfUser = new IndicationOfUser(new User(login, "password", false), CounterOf.HEATING, new Indication(LocalDate.now(), 10.0));
        indicationOfUserDAO.add(indicationOfUser);

        // Act
        Optional<IndicationOfUser> result = indicationOfUserDAO.getByLogin(login);

        // Assert
        assertThat(result)
                .isPresent()
                .get()
                .isEqualTo(indicationOfUser);
    }

    /**
     * Test case for the {@link IndicationOfUserDAOImpl#getByLogin(String)} method
     * with a non-existing IndicationOfUser, should return an empty Optional.
     */
    @Test
    void getByLogin_nonExistingIndicationOfUser_returnsEmptyOptional() {
        // Arrange
        String login = "nonexistent";

        // Act
        Optional<IndicationOfUser> result = indicationOfUserDAO.getByLogin(login);

        // Assert
        assertThat(result).isEmpty();
    }

    /**
     * Test case for the {@link IndicationOfUserDAOImpl#getAll()} method,
     * should return a list containing all stored IndicationsOfUser.
     */
    @Test
    void getAll_returnsListOfIndicationsOfUser() {
        // Arrange
        IndicationOfUser indicationOfUser1 = new IndicationOfUser(new User("user1", "password", false), CounterOf.HEATING, new Indication(LocalDate.now(), 10.0));
        IndicationOfUser indicationOfUser2 = new IndicationOfUser(new User("user2", "password", false), CounterOf.HOT_WATER, new Indication(LocalDate.now(), 120.0));
        indicationOfUserDAO.add(indicationOfUser1);
        indicationOfUserDAO.add(indicationOfUser2);

        // Act
        List<IndicationOfUser> result = indicationOfUserDAO.getAll();

        // Assert
        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .contains(indicationOfUser1, indicationOfUser2);
    }

    /**
     * Test case for the {@link IndicationOfUserDAOImpl#add(IndicationOfUser)} method,
     * should add the IndicationOfUser to the list.
     */
    @Test
    void add_indicationOfUserToList() {
        // Arrange
        IndicationOfUser indicationOfUser = new IndicationOfUser(new User("newUser", "password", false), CounterOf.HEATING, new Indication(LocalDate.now(), 10.0));

        // Act
        indicationOfUserDAO.add(indicationOfUser);

        // Assert
        assertThat(indicationOfUserDAO.getAll())
                .hasSize(1)
                .contains(indicationOfUser);
    }

    /**
     * Test case for the {@link IndicationOfUserDAOImpl#getByLogin(String)} method
     * using Mockito to mock behavior.
     */
    @Test
    void getByLogin_withMockito() {
        // Arrange
        String login = "user1";
        IndicationOfUser indicationOfUser = new IndicationOfUser(new User(login, "password", false), CounterOf.HEATING, new Indication(LocalDate.now(), 10.0));

        // Mock the behavior of the getByLogin method
        when(indicationOfUserDAO.getByLogin(login)).thenReturn(Optional.of(indicationOfUser));

        // Act
        Optional<IndicationOfUser> result = indicationOfUserDAO.getByLogin(login);

        // Assert
        assertThat(result)
                .isPresent()
                .get()
                .isEqualTo(indicationOfUser);

        // Verify that the method was called
        verify(indicationOfUserDAO, times(1)).getByLogin(login);
    }
}