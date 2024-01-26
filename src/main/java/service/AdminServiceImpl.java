package service;

import dao.DAO;
import dao.UserDAOImpl;
import model.User;
import model.UserEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdminServiceImpl implements AdminService {
    private final List<UserEvent> eventList = new ArrayList<>();
    private final DAO<User> userDAO = new UserDAOImpl();

    @Override
    public void addEvent(String login, String message) {
        LocalDate date = LocalDate.now();
        UserEvent userEvent = new UserEvent(date, userDAO.getByLogin(login).orElseThrow(), message);
        eventList.add(userEvent);
    }

    @Override
    public String getAllEvents() {
        StringBuilder result = new StringBuilder("Log for all users:\n");
        for (UserEvent userEvent : eventList) {
            format(result, userEvent);
        }
        return result.toString();
    }

    @Override
    public String getAllEventsForUser(String login) {
        StringBuilder result = new StringBuilder("Log for " + login + ":\n");
        List<UserEvent> eventsForUser = eventList
                .stream()
                .filter(e -> e.user().login().equals(login))
                .toList();
        for (UserEvent userEvent : eventsForUser) {
            format(result, userEvent);
        }
        return result.toString();
    }

    private void format(StringBuilder result, UserEvent userEvent) {
        result.append(userEvent.date())
                .append(" | ")
                .append(userEvent.user().login())
                .append(" | ")
                .append(userEvent.message())
                .append("\n");
    }
}
