package com.makeev.monitoring_service.dao;

import com.makeev.monitoring_service.model.Counter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CounterDAOImpl implements DAO<Counter> {
    private final List<Counter> listOfCounter = new ArrayList<>();
    private int sizeOfList;

    public CounterDAOImpl() {
        listOfCounter.add(new Counter("Heating"));
        listOfCounter.add(new Counter("Hot Water"));
        listOfCounter.add(new Counter("Cold Water"));
        sizeOfList = listOfCounter.size();
    }

    @Override
    public Optional<Counter> getBy(String nameOfCounter) {
        for (Counter counter : listOfCounter) {
            if (counter.name().equals(nameOfCounter)) {
                return Optional.of(counter);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Counter> getAll() {
        return listOfCounter;
    }

    @Override
    public void add(Counter counter) {
        listOfCounter.add(counter);
        sizeOfList = listOfCounter.size();
    }

    public int getSizeOfList() {
        return sizeOfList ;
    }

    public Optional<Counter> getByIndex(int index) {
        index -= 1;
        if (index < sizeOfList) {
            return Optional.of(listOfCounter.get(index));
        }
        return Optional.empty();
    }
}
