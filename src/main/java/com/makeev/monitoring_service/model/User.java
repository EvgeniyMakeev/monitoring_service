package com.makeev.monitoring_service.model;

//TODO Add password encryption

import java.util.List;
import java.util.Map;

/**
 * A record representing a user with login credentials and administrative rights.
 * @author Evgeniy Makeev
 * @version 1.4
 */
public record User(String login, String password, Map<Counter,
        List<Indication>> mapOfIndicationOfCounter, Boolean admin) {
}
