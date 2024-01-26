package service;

public interface AdminService {
    void addEvent(String login, String message);
    String getAllEvents();
    String getAllEventsForUser(String login);

}
