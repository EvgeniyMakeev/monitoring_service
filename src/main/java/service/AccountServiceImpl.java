package service;

import constants.CounterOf;
import dao.DAO;
import dao.IndicationOfUserDAOImpl;
import dao.UserDAOImpl;
import model.Indication;
import model.IndicationOfUser;
import model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AccountServiceImpl implements AccountService{

    private final DAO<User> userDAO = new UserDAOImpl();
    private final DAO<IndicationOfUser> indicationDAO = new IndicationOfUserDAOImpl();

    @Override
    public boolean addUser(String login, String pass) {
        for (User user : userDAO.getAll()) {
            if (user.login().equals(login)) {
                return false;
            }
        }
        userDAO.add(new User(login, pass, false));
        return true;
    }
    @Override
    public boolean findAndCheckUser(String login, String pass) {
        for (User user : userDAO.getAll()) {
            if (user.login().equals(login) && user.password().equals(pass)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public User getUserByLogin(Optional<String> login) {
        return userDAO.getByLogin(login.get()).get();
    }
    @Override
    public int addIndicationOfUser(String login, Integer year, Integer month, CounterOf counterOf, Double value) {
        User user = userDAO.getByLogin(login).get();
        LocalDate date = LocalDate.of(year, month, 1);
        Indication indication = new Indication(date, value);
        int answer = 0;
        if (indicationDAO.getByLogin(login).isEmpty()) {
            indicationDAO.add(new IndicationOfUser(user, counterOf, indication));
            answer = 1;
        }
        boolean bigger = false;
        for (IndicationOfUser indicationOfUser : indicationDAO.getAll()) {
            if (indicationOfUser.user().equals(user) && indicationOfUser.counterOf().equals(counterOf)) {
                if (indicationOfUser.indication().date().isBefore(date) &&
                        indicationOfUser.indication().value() <= value) {
                    bigger = true;
                }
            }
        }
        if (bigger) {
            indicationDAO.add(new IndicationOfUser(user, counterOf, indication));
            answer = 1;
        }
        return answer;
    }

    @Override
    public String getAllIndicationsForUser(String login) {
        StringBuilder result = new StringBuilder("Indications submission history of " + login + ":\n");
        List<IndicationOfUser> listForUser = indicationDAO.getAll()
                .stream()
                .filter(i -> i.user().login().equals(login))
                .toList();
        if (!listForUser.isEmpty()) {
            for (IndicationOfUser indicationOfUser : listForUser) {
                format(result, indicationOfUser);
            }
        } else {
            result.append("None.");
        }
        return result.toString();
    }
    @Override
    public String getAllIndicationsForUserForMonth(String login, Integer year, Integer month) {
        LocalDate date = LocalDate.of(year, month, 1);
        StringBuilder result = new StringBuilder("Indications submission history for " + date.getMonth() + " - " + date.getYear() + ":\n");
        List<IndicationOfUser> listForUser = indicationDAO.getAll()
                .stream()
                .filter(i -> i.user().login().equals(login))
                .filter(i -> i.indication().date().equals(date))
                .toList();
        if (!listForUser.isEmpty()) {
            for (IndicationOfUser indicationOfUser : listForUser) {
                format(result, indicationOfUser);
            }
        } else {
            result.append("None.");
        }
        return result.toString();
    }

    @Override
    public String getCurrentMeter(String login, CounterOf counterOf) {
        StringBuilder result = new StringBuilder("Current meter indications:\n");
        List<IndicationOfUser> listForUser = indicationDAO.getAll()
                .stream()
                .filter(i -> i.user().login().equals(login))
                .filter(i -> i.counterOf().equals(counterOf))
                .toList();
        if (!listForUser.isEmpty()) {
            int lastIndex = listForUser.size();
            format(result,listForUser.get(lastIndex - 1));
        } else {
            result.append("None.");
        }
        return result.toString();
    }

    @Override
    public boolean isAdmin(String login) {
        return userDAO.getByLogin(login).get().admin();
    }

    @Override
    public boolean findUser(String login) {
        return userDAO.getByLogin(login).isPresent();
    }

    @Override
    public String getAllIndications() {
        StringBuilder result = new StringBuilder("Indications submission history for all users:\n");
        if (!indicationDAO.getAll().isEmpty()) {
            for (IndicationOfUser indicationOfUser : indicationDAO.getAll()) {
                format(result, indicationOfUser);
            }
        } else {
            result.append("None.");
        }
        return result.toString();
    }

    private void format(StringBuilder result, IndicationOfUser indicationOfUser) {
        result.append(indicationOfUser.counterOf())
                .append(" | ")
                .append(indicationOfUser.indication().date().getYear())
                .append(" - ")
                .append(indicationOfUser.indication().date().getMonth())
                .append(" | ")
                .append(indicationOfUser.indication().value())
                .append("\n");
    }
}
