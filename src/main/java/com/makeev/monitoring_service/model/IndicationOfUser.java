package com.makeev.monitoring_service.model;

import com.makeev.monitoring_service.constants.CounterOf;

/**
 * A record representing an indication of a user, associated counter type, and corresponding indication details.
 * @author Evgeniy Makeev
 * @version 1.4
 */
public record IndicationOfUser(User user, CounterOf counterOf, Indication indication) {
}
