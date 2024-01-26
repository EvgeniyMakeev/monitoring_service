package model;

import constants.CounterOf;

public record IndicationOfUser(User user, CounterOf counterOf, Indication indication) {
}
