package com.makeev.monitoring_service;

import com.makeev.monitoring_service.dao.CounterDAO;
import com.makeev.monitoring_service.dao.UserDAO;
import com.makeev.monitoring_service.exceptions.EmptyException;
import com.makeev.monitoring_service.exceptions.LoginAlreadyExistsException;
import com.makeev.monitoring_service.exceptions.VerificationException;
import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.model.User;
import com.makeev.monitoring_service.model.UserEvent;
import com.makeev.monitoring_service.service.AdminService;
import com.makeev.monitoring_service.service.IndicationService;
import com.makeev.monitoring_service.utils.ConnectionManager;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MonitoringServiceTests {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:latest");

    private static ConnectionManager testConnectionManager;

    @BeforeAll
    public static void setUp() throws Exception {
        String jdbcUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();

        testConnectionManager = new ConnectionManagerImpl(jdbcUrl, username, password);

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml",
                    new ClassLoaderResourceAccessor(), new JdbcConnection(connection));
            liquibase.update("");
        }
    }

    UserDAO userDAO = new UserDAO(testConnectionManager);
    CounterDAO counterDAO = new CounterDAO(testConnectionManager);
    AdminService adminService = new AdminService(testConnectionManager);
    IndicationService indicationService = new IndicationService(testConnectionManager);


    @Order(1)
    @Test
    public void testAddCounter() throws Exception {
        Counter counter = counterDAO.add("TestCounter");

        assertNotNull(counter);
        assertEquals("TestCounter", counter.name());
        assertNotNull(counter.id());

        try (Connection connection = testConnectionManager.open();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM non_public.counters WHERE id = " + counter.id());
        }
    }

    @Order(2)
    @Test
    public void testGetBy() throws Exception {
        try (Connection connection = testConnectionManager.open();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO non_public.counters (name) VALUES ('TestCounter')");
        }

        List<Counter> counters = counterDAO.getAll();
        Counter savedCounter = counters.get(3);
        Optional<Counter> retrievedCounter = counterDAO.getBy(savedCounter.id());

        assertTrue(retrievedCounter.isPresent());
        assertEquals(savedCounter.id(), retrievedCounter.get().id());
        assertEquals(savedCounter.name(), retrievedCounter.get().name());

        try (Connection connection = testConnectionManager.open();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM non_public.counters WHERE id = " + savedCounter.id());
        }
    }

    @Order(3)
    @Test
    public void testGetAll() throws Exception {
        try (Connection connection = testConnectionManager.open();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO non_public.counters (name) VALUES ('TestCounter1'), ('TestCounter2')");
        }

        List<Counter> counters = counterDAO.getAll();

        assertNotNull(counters);
        assertEquals(5, counters.size());
        assertEquals("TestCounter1", counters.get(3).name());
        assertEquals("TestCounter2", counters.get(4).name());

        try (Connection connection = testConnectionManager.open();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM non_public.counters WHERE name LIKE 'TestCounter%'");
        }
    }

    @Order(4)
    @Test
    public void testGetNumberOfCounters() throws Exception {
        try (Connection connection = testConnectionManager.open();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO non_public.counters (name) VALUES ('TestCounter3'), ('TestCounter4')");
        }

        int numberOfCounters = counterDAO.getNumberOfCounters();

        assertEquals(5, numberOfCounters);

        try (Connection connection = testConnectionManager.open();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM non_public.counters WHERE name LIKE 'TestCounter%'");
        }
    }

    @Order(5)
    @Test
    public void testAddUser() {
        User user = new User("test_user", "password", false);
        userDAO.add(user);

        Optional<User> retrievedUser = userDAO.getBy("test_user");
        assertTrue(retrievedUser.isPresent());
        assertEquals("test_user", retrievedUser.get().login());
        assertEquals("password", retrievedUser.get().password());
        assertFalse(retrievedUser.get().admin());
    }

    @Order(6)
    @Test
    public void testGetByLogin() {
        Optional<User> user = userDAO.getBy("test_user");

        assertTrue(user.isPresent());
        assertEquals("test_user", user.get().login());
        assertEquals("password", user.get().password());
        assertFalse(user.get().admin());
    }

    @Order(7)
    @Test
    public void testGetAllUsers() {
        List<User> users = userDAO.getAll();
        assertEquals(2, users.size());
        assertEquals("test_user", users.get(1).login());
        assertEquals("password", users.get(1).password());
        assertFalse(users.get(1).admin());
    }

    @Order(8)
    @Test
    public void testExistByLogin() {
        assertThrows(LoginAlreadyExistsException.class, () -> userDAO.existByLogin("test_user"));
    }

    @Order(9)
    @Test
    public void testIsAdmin() {
        assertFalse(userDAO.isAdmin("test_user"));
    }

    @Order(10)
    @Test
    public void testCheckCredentials() {
        assertDoesNotThrow(() -> userDAO.checkCredentials("test_user", "password"));
        assertThrows(VerificationException.class, () -> userDAO.checkCredentials("test_user", "wrong_password"));
    }

    @Order(11)
    @Test
    public void testAddEvent() throws SQLException {
        adminService.addEvent("test_user", "Test event");

        List<UserEvent> events = adminService.getAllEvents();
        assertEquals(1, events.size());

        UserEvent event = events.get(0);
        assertEquals(LocalDate.now().toString(), event.date());
        assertEquals("test_user", event.login());
        assertEquals("Test event", event.message());

        try (var connection = testConnectionManager.open();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM non_public.user_events");
        }
    }

    @Order(12)
    @Test
    public void testGetAllEventsEmpty() {
        assertThrows(EmptyException.class, () -> adminService.getAllEvents());
    }

    @Order(12)
    @Test
    public void testGetAllEventsForUserEmpty() {
        assertThrows(EmptyException.class, () -> adminService.getAllEventsForUser("non_existing_user"));
    }

    @Test
    public void testGetAllIndicationsEmpty() {
        assertThrows(EmptyException.class, indicationService::getAllIndications);
    }

    @Test
    public void testGetAllIndicationsForUserEmpty() {
        assertThrows(EmptyException.class, () -> indicationService.getAllIndicationsForUser("non_existing_user"));
    }

    @Test
    public void testGetAllIndicationsForUserForMonthEmpty() {
        assertThrows(EmptyException.class, () -> indicationService.getAllIndicationsForUserForMonth("test_user", LocalDate.of(2024, 2, 1)));
    }
}