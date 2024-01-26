package service;

import constants.CounterOf;

public interface AccountService {
    boolean addUser(String login, String pass);

    boolean findAndCheckUser(String login, String pass);

    int addIndicationOfUser(String login, Integer year, Integer month, CounterOf counterOf, Double value);

    String getAllIndicationsForUser(String login);

    String getAllIndicationsForUserForMonth(String login, Integer year, Integer month);

    String getCurrentMeter(String s, CounterOf counterOf);

    boolean isAdmin(String login);

    boolean findUser(String login);

    String getAllIndications();
}
