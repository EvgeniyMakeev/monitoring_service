package com.makeev.monitoring_service.model;

//TODO Add password encryption

/**
 * A record representing a user with login credentials and administrative rights.
 * @author Evgeniy Makeev
 * @version 1.4
 */
public record User(String login, String password, Boolean admin) {
}
