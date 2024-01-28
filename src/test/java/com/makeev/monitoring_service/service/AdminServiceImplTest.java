package com.makeev.monitoring_service.service;

import com.makeev.monitoring_service.model.User;
import com.makeev.monitoring_service.model.UserEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the {@link AdminServiceImpl} class.
 */
@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

    @InjectMocks
    private AdminServiceImpl adminServiceTest;

    /**
     * Initializes mock objects before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for the {@link AdminServiceImpl#addEvent(User, String)} method,
     * should add a {@link UserEvent} to the list.
     */
    @Test
    void addEvent_shouldAddUserEventToList() {
        // Arrange
        User user = new User("testUser", "password", false);
        String message = "Test message";

        // Act
        adminServiceTest.addEvent(user, message);

        // Assert
        List<UserEvent> eventList = adminServiceTest.getEventList();
        assertThat(eventList).hasSize(1);
        UserEvent userEvent = eventList.get(0);
        assertThat(userEvent.user()).isEqualTo(user);
        assertThat(userEvent.message()).isEqualTo(message);
        assertThat(userEvent.date()).isEqualTo(LocalDate.now());
    }

    /**
     * Test case for the {@link AdminServiceImpl#getAllEvents()} method,
     * should return a formatted string containing log for all users.
     */
    @Test
    void getAllEvents_shouldReturnFormattedString() {
        // Arrange
        User user = new User("testUser", "password", false);
        String message = "Test message";
        adminServiceTest.addEvent(user, message);

        // Act
        String result = adminServiceTest.getAllEvents();

        // Assert
        assertThat(result).contains("Log for all users:");
        assertThat(result).contains(user.login(), message);
    }

    /**
     * Test case for the {@link AdminServiceImpl#getAllEventsForUser(String)} method,
     * should return a formatted string containing log for a specific user.
     */
    @Test
    void getAllEventsForUser_shouldReturnFormattedString() {
        // Arrange
        User user = new User("testUser", "password", false);
        String message = "Test message";
        adminServiceTest.addEvent(user, message);

        // Act
        String result = adminServiceTest.getAllEventsForUser(user.login());

        // Assert
        assertThat(result).contains("Log for " + user.login() + ":");
        assertThat(result).contains(message);
    }
}