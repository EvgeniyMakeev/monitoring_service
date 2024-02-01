package com.makeev.monitoring_service.model;

import java.time.LocalDate;

/**
 * A record representing an event related to a user, including the date, the user involved, and a message.
 */
public record UserEvent(LocalDate date, String login, String message) {
}
