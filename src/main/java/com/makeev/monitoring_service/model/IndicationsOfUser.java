package com.makeev.monitoring_service.model;

/**
 * A record representing an indication with a date and a corresponding value.
 */
public record IndicationsOfUser(String login, Counter counter, Indication indication) {
}
