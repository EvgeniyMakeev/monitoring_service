package com.makeev.monitoring_service.model;

/**
 * A record representing an event related to a user, including the date, the user involved, and a message.
 */
public record UserEvent(String date, String message) {
}
