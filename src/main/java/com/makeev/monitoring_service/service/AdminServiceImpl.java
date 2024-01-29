package com.makeev.monitoring_service.service;

import com.makeev.monitoring_service.model.User;
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
     * @param user    The user for whom the event is added.
     * @param message The message describing the event.
     */
    @Override
    public void addEvent(User user, String message) {
        LocalDate date = LocalDate.now();
        UserEvent userEvent = new UserEvent(date, user, message);
        eventList.add(userEvent);
    }

    /**
     * Retrieves the submission history of all events.
     *
     * @return A formatted string representing the submission history of all events.
     */
    @Override
    public String getAllEvents() {
        StringBuilder result = new StringBuilder("Log for all users:\n");
        for (UserEvent userEvent : eventList) {
            format(result, userEvent);
        }
        return result.toString();
    }

    /**
     * Retrieves the submission history of events for a specific user.
     *
     * @param login The login of the user.
     * @return A formatted string representing the submission history of events for the user.
     */
    @Override
    public String getAllEventsForUser(String login) {
        StringBuilder result = new StringBuilder("Log for " + login + ":\n");
        List<UserEvent> eventsForUser = eventList
                .stream()
                .filter(e -> e.user().login().equals(login))
                .toList();
        for (UserEvent userEvent : eventsForUser) {
            format(result, userEvent);
        }
        return result.toString();
    }

    /**
     * Retrieves the list of all events.
     *
     * @return A list containing {@link UserEvent} objects representing all events.
     */
    @Override
    public List<UserEvent> getEventList() {
        return eventList;
    }

    /**
     * Formats a {@link UserEvent} object into a string and appends it to the result.
     *
     * @param result     The StringBuilder to which the formatted string is appended.
     * @param userEvent  The UserEvent to be formatted.
     */
    private void format(StringBuilder result, UserEvent userEvent) {
        result.append(userEvent.date())
                .append(" | ")
                .append(userEvent.user().login())
                .append(" | ")
                .append(userEvent.message())
                .append("\n");
    }
}
