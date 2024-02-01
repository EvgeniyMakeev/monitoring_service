package com.makeev.monitoring_service.model;

import java.time.LocalDate;

/**
 * A record representing an indication with a date and a corresponding value.
 */
public record Indication(LocalDate date, Double value) {
}
