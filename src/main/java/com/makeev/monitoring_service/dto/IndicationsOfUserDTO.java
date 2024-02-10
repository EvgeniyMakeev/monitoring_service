package com.makeev.monitoring_service.dto;

import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.model.Indication;

/**
 * A record representing an indication with a date and a corresponding value.
 */
public record IndicationsOfUserDTO(String login, Counter counter, Indication indication) {
}
