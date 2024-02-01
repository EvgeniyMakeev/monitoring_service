package com.makeev.monitoring_service.dao;

import com.makeev.monitoring_service.model.Counter;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CounterDAO}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CounterDAO Test")
public class CounterDAOTest {

    @Mock
    private Counter counter1;

    @Mock
    private Counter counter2;

    @InjectMocks
    private CounterDAO counterDAO;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Get Counter by Name - Success")
    void testGetCounterByName_Success() {
        // Given
        String nameToFind = "Hot Water";
        when(counter1.name()).thenReturn("Heating");
        when(counter2.name()).thenReturn(nameToFind);


        // When
        Optional<Counter> result = counterDAO.getBy(nameToFind);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo(counter2.name());
    }

    @Test
    @DisplayName("Get Counter by Name - Not Found")
    void testGetCounterByName_NotFound() {
        // Given
        String nameToFind = "Non-existent Counter";
        when(counter1.name()).thenReturn("Heating");
        when(counter2.name()).thenReturn("Hot Water");

        // When
        Optional<Counter> result = counterDAO.getBy(nameToFind);

        // Then
        assertThat(result).isEmpty();
    }


    @Test
    @DisplayName("Add Counter")
    void testAddCounter() {
        // Given
        Counter newCounter = new Counter("New Counter");

        // When
        counterDAO.add(newCounter);

        // Then
        assertThat(counterDAO.getAll()).contains(newCounter);
        assertThat(counterDAO.getSizeOfList()).isEqualTo(4);
    }

    @Test
    @DisplayName("Get Counter by Index - Success")
    void testGetCounterByIndex_Success() {
        // Given
        int indexToFind = 2;
        when(counter1.name()).thenReturn("Heating");
        when(counter2.name()).thenReturn("Hot Water");

        // When
        Optional<Counter> result = counterDAO.getByIndex(indexToFind);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo(counter2.name());
    }

    @Test
    @DisplayName("Get Counter by Index - Index Out of Bounds")
    void testGetCounterByIndex_IndexOutOfBounds() {
        // Given
        int indexToFind = 5;
        when(counter1.name()).thenReturn("Heating");
        when(counter2.name()).thenReturn("Hot Water");

        // When
        Optional<Counter> result = counterDAO.getByIndex(indexToFind);

        // Then
        assertThat(result).isEmpty();
    }
}
