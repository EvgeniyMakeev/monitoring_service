package com.makeev.monitoring_service.service;

import com.makeev.monitoring_service.model.UserEvent;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AdminService}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("AdminService Test")
public class AdminServiceTest {

    @Mock
    private AdminService adminService;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Adding Event")
    void addEvent() {
        // Given
        String login = "testUser";
        String message = "Test message";

        // When
        adminService.addEvent(login, message);

        // Then
        verify(adminService, times(1)).addEvent(login, message);
        verifyNoMoreInteractions(adminService);
    }

    @Test
    @DisplayName("Getting All Events")
    void getAllEvents() {
        // Given
        String login1 = "user1";
        String login2 = "user2";
        String message1 = "Message 1";
        String message2 = "Message 2";

        when(adminService.getAllEvents())
                .thenReturn(List.of(
                        new UserEvent(LocalDate.now(), login1, message1),
                        new UserEvent(LocalDate.now(), login2, message2)
                ));

        // When
        List<UserEvent> eventList = adminService.getAllEvents();

        // Then
        assertThat(eventList).hasSize(2);

        UserEvent event1 = eventList.get(0);
        assertThat(event1.login()).isEqualTo(login1);
        assertThat(event1.message()).isEqualTo(message1);

        UserEvent event2 = eventList.get(1);
        assertThat(event2.login()).isEqualTo(login2);
        assertThat(event2.message()).isEqualTo(message2);

        verify(adminService, times(1)).getAllEvents();
        verifyNoMoreInteractions(adminService);
    }

    @Test
    @DisplayName("Getting All Events for User")
    void getAllEventsForUser() {
        // Given
        String login1 = "user";
        String message1 = "Message ";

        when(adminService.getAllEventsForUser(login1))
                .thenReturn(List.of(
                        new UserEvent(LocalDate.now(), login1, message1),
                        new UserEvent(LocalDate.now(), login1, "Another message for user")
                ));

        // When
        List<UserEvent> eventsForUser1 = adminService.getAllEventsForUser(login1);

        // Then
        assertThat(eventsForUser1).hasSize(2);

        UserEvent event1 = eventsForUser1.get(0);
        assertThat(event1.login()).isEqualTo(login1);
        assertThat(event1.message()).isEqualTo(message1);

        UserEvent event2 = eventsForUser1.get(1);
        assertThat(event2.login()).isEqualTo(login1);
        assertThat(event2.message()).isEqualTo("Another message for user");

        verify(adminService, times(1)).getAllEventsForUser(login1);
        verifyNoMoreInteractions(adminService);
    }
}
