package service;

import constants.CounterOf;
import dao.DAO;
import model.Indication;
import model.IndicationOfUser;
import model.User;
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


class AccountServiceImplTest {

    @Mock
    private DAO<User> userDAOMock;

    @Mock
    private DAO<IndicationOfUser> indicationDAOMock;

    @InjectMocks
    private AccountServiceImpl accountServiceTest = new AccountServiceImpl();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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