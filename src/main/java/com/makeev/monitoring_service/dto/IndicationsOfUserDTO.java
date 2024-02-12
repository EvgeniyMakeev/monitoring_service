package com.makeev.monitoring_service.dto;


/**
 * A record representing an indication with a date and a corresponding value.
 */
public record IndicationsOfUserDTO(String login, String counterName, int year, int month, Double value) {
}
