package com.makeev.monitoring_service.model;

import java.util.List;
import java.util.Map;

/**
 * A record representing a user with login credentials and administrative rights.
 */
public record User(String login, String password, Map<Counter,
        List<Indication>> mapOfIndicationOfCounter, Boolean admin) {
}
