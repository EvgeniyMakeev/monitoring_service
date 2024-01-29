package com.makeev.monitoring_service.service;

import com.makeev.monitoring_service.model.User;
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
     * @param user    The user for whom the event is added.
     * @param message The message describing the event.
     */
    void addEvent(User user, String message);

    /**
     * Retrieves the submission history of all events.
     *
     * @return A formatted string representing the submission history of all events.
     */
    String getAllEvents();

    /**
     * Retrieves the submission history of events for a specific user.
     *
     * @param login The login of the user.
     * @return A formatted string representing the submission history of events for the user.
     */
    String getAllEventsForUser(String login);

    /**
     * Retrieves the list of all events.
     *
     * @return A list containing {@link UserEvent} objects representing all events.
     */
    List<UserEvent> getEventList();
}
