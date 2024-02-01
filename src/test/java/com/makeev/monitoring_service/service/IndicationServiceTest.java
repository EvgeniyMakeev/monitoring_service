package com.makeev.monitoring_service.service;

import com.makeev.monitoring_service.dao.UserDAO;
import com.makeev.monitoring_service.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link IndicationService}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("IndicationService Test")
class IndicationServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private IndicationService indicationService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Add User")
    void testAddUser() {
        // Given
        String login = "john";
        String password = "password";
        userDAO.add(new User(login, password, new HashMap<>(), false));

        // When
        indicationService.addUser(login, password);

        // Then
        verify(userDAO).add(new User(login, password, new HashMap<>(), false));
    }
}