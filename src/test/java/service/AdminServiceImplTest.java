package service;

import model.User;
import model.UserEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @InjectMocks
    private AdminServiceImpl adminServiceTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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