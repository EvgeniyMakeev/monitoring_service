package service;

import model.User;
import model.UserEvent;

import java.util.List;

public interface AdminService {
    void addEvent(User user, String message);
    String getAllEvents();
    String getAllEventsForUser(String login);

    List<UserEvent> getEventList();
}
