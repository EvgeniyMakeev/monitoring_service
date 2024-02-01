package com.makeev.monitoring_service.service;

import com.makeev.monitoring_service.model.UserEvent;

import java.util.List;

/**
 * Service interface providing methods for managing admin-related events.
 * @author Evgeniy Makeev
 * @version 1.4
 */
public interface AdminService {
    /**
     * Adds an event for a specific user with a corresponding message.
     *
     * @param login The login of user for whom the event is added.
     * @param message The message describing the event.
     */
    void addEvent(String login, String message);

    /**
     * Retrieves the submission history of all events.
     *
     * @return A formatted string representing the submission history of all events.
     */
    List<UserEvent> getAllEvents();

    /**
     * Retrieves the submission history of events for a specific user.
     *
     * @param login The login of the user.
     * @return A formatted string representing the submission history of events for the user.
     */
    List<UserEvent> getAllEventsForUser(String login);

}
