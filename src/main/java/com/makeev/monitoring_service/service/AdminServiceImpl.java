package com.makeev.monitoring_service.service;

import com.makeev.monitoring_service.model.UserEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link AdminService} interface that manages admin-related events.
 * @author Evgeniy Makeev
 * @version 1.4
 */
public class AdminServiceImpl implements AdminService {

    /**
     * List to store user events.
     */
    private final List<UserEvent> eventList = new ArrayList<>();

    /**
     * Adds an event for a specific user with a corresponding message.
     *
     * @param login The user of login for whom the event is added.
     * @param message The message describing the event.
     */
    @Override
    public void addEvent(String login, String message) {
        LocalDate date = LocalDate.now();
        UserEvent userEvent = new UserEvent(date, login, message);
        eventList.add(userEvent);
    }

    /**
     * Retrieves the submission history of all events.
     *
     * @return A formatted string representing the submission history of all events.
     */
    @Override
    public List<UserEvent> getAllEvents() {
        return eventList;
    }

    /**
     * Retrieves the submission history of events for a specific user.
     *
     * @param login The login of the user.
     * @return A formatted string representing the submission history of events for the user.
     */
    @Override
    public List<UserEvent> getAllEventsForUser(String login) {
       return eventList
               .stream()
               .filter(e -> e.login().equals(login))
               .toList();
    }

}
