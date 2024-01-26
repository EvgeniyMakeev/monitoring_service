package dao;

import model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDAOImplTest {

    @Test
    void getByLogin_existingUser_returnsOptionalUser() {
        // Arrange
        UserDAOImpl userDAO = new UserDAOImpl();
        String login = "admin";

        // Act
        Optional<User> result = userDAO.getByLogin(login);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("admin", result.get().login());
    }

    @Test
    void getByLogin_nonExistingUser_returnsEmptyOptional() {
        // Arrange
        UserDAOImpl userDAO = new UserDAOImpl();
        String login = "nonexistent";

        // Act
        Optional<User> result = userDAO.getByLogin(login);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getAll_returnsListOfUsers() {
        // Arrange
        UserDAOImpl userDAO = new UserDAOImpl();

        // Act
        List<User> result = userDAO.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size()); // Assuming the initial list has one user
    }

    @Test
    void add_userToList() {
        // Arrange
        UserDAOImpl userDAO = new UserDAOImpl();
        User newUser = new User("newUser", "password", false);

        // Act
        userDAO.add(newUser);

        // Assert
        assertEquals(2, userDAO.getAll().size()); // Assuming the initial list has one user
        assertTrue(userDAO.getAll().contains(newUser));
    }

    // Mockito example to mock behavior
    @Test
    void getByLogin_withMockito() {
        // Arrange
        UserDAOImpl userDAO = Mockito.spy(new UserDAOImpl());
        String login = "admin";

        // Mock the behavior of the getByLogin method
        when(userDAO.getByLogin(login)).thenReturn(Optional.of(new User(login, "admin", true)));

        // Act
        Optional<User> result = userDAO.getByLogin(login);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(login, result.get().login());

        // Verify that the method was called
        verify(userDAO, times(1)).getByLogin(login);
    }
}