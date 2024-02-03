package com.makeev.monitoring_service.dao;

import com.makeev.monitoring_service.model.Counter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The {@code CounterDAO} class is responsible for managing the persistence
 * of Counter entities. It provides methods to retrieve, add, and query Counters.
 */
public class CounterDAO implements DAO<Counter, String> {

    private final static CounterDAO INSTANCE = new CounterDAO();

    public static CounterDAO getInstance() {
        return INSTANCE;
    }

    /**
     * The list of Counter entities managed by the DAO.
     */
    private final List<Counter> listOfCounter = new ArrayList<>();

    /**
     * The size of the list of Counter entities.
     */
    private int sizeOfList;

    /**
     * Default constructor that initializes the list of Counters with default values.
     */
    public CounterDAO() {
        listOfCounter.add(new Counter("Heating"));
        listOfCounter.add(new Counter("Hot Water"));
        listOfCounter.add(new Counter("Cold Water"));
        sizeOfList = listOfCounter.size();
    }

    /**
     * Adds a Counter entity to the list.
     *
     * @param counter The Counter entity to add.
     */
    @Override
    public void add(Counter counter) {
        listOfCounter.add(counter);
        sizeOfList = listOfCounter.size();
    }

    /**
     * Retrieves a Counter entity by its name.
     *
     * @param nameOfCounter The name of the Counter to retrieve.
     * @return An {@code Optional} containing the Counter if found, or empty if not found.
     */
    @Override
    public Optional<Counter> getBy(String nameOfCounter) {


        for (Counter counter : listOfCounter) {
            if (counter.name().equals(nameOfCounter)) {
                return Optional.of(counter);
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieves a list of all Counter entities.
     *
     * @return The list of all Counter entities.
     */
    @Override
    public List<Counter> getAll() {
        return listOfCounter;
    }

    /**
     * Gets the size of the list of Counter entities.
     *
     * @return The size of the list.
     */
    public int getSizeOfList() {
        return sizeOfList;
    }

    /**
     * Retrieves a Counter entity by its index in the list.
     *
     * @param index The index of the Counter to retrieve.
     * @return An {@code Optional} containing the Counter if found, or empty if not found.
     */
    public Optional<Counter> getByIndex(int index) {
        index -= 1;
        if (index < sizeOfList) {
            return Optional.of(listOfCounter.get(index));
        }
        return Optional.empty();
    }
}
