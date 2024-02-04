package com.makeev.monitoring_service.model;

/**
 * A record representing a user with login credentials and administrative rights.
 */
public record User(String login, String password, Boolean admin) {
}
