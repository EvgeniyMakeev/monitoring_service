package model;

import java.time.LocalDate;

public record UserEvent(LocalDate date, User user, String message) {
}
