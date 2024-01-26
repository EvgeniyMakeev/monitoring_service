package service;

import model.User;

public interface AdminService {
    void addEvent(User user, String message);
    String getAllEvents();
    String getAllEventsForUser(String login);

}
