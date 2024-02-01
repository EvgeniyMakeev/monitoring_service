package com.makeev.monitoring_service.service;

import com.makeev.monitoring_service.dao.CounterDAOImpl;
import com.makeev.monitoring_service.dao.UserDAOImpl;
import com.makeev.monitoring_service.exceptions.EmptyException;
import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.model.Indication;
import com.makeev.monitoring_service.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndicationServiceImpl implements IndicationService {

    /**
     * DAO for managing Users.
     */
    public final UserDAOImpl userDAO = new UserDAOImpl();
    public final CounterDAOImpl counterDAO = new CounterDAOImpl();

    public void addUser(String login, String password) {
        userDAO.add(new User(login, password, new HashMap<>(), false));
    }

    public Map<Counter, List<Indication>> getAllIndicationsForUserForMonth
            (String login, LocalDate date) throws EmptyException {

        Map<Counter, List<Indication>> mapOfIndicationForUser =
                userDAO.getAllIndicationsForUser(login);

        Map<Counter, List<Indication>> mapOfIndicationOfMonth = new HashMap<>();

        if (mapOfIndicationForUser.isEmpty()) {
            throw new EmptyException();
        } else {
            mapOfIndicationForUser
                    .forEach((k,v) -> {
                        for (Indication indication : v) {
                            if (indication.date().equals(date)) {
                                mapOfIndicationOfMonth.put(k, v);
                            }
                        }
                    } );
            if (mapOfIndicationOfMonth.isEmpty()) {
                throw new EmptyException();
            }
        }

        return mapOfIndicationForUser;
    }

    public Map<Counter, Indication> getCurrentIndication(String login) throws EmptyException {
        Map<Counter, List<Indication>> mapOfIndicationForUser =
                userDAO.getAllIndicationsForUser(login);

        Map<Counter, Indication> mapOfCurrentIndication = new HashMap<>();

        if (mapOfIndicationForUser.isEmpty()) {
            throw new EmptyException();
        } else {
            mapOfIndicationForUser
                    .forEach((k,v) -> {
                        int lastIndex = v.size() - 1;
                        mapOfCurrentIndication.put(k, v.get(lastIndex));
                    } );
            if (mapOfCurrentIndication.isEmpty()) {
                throw new EmptyException();
            }
        }

        return mapOfCurrentIndication;
    }

}
