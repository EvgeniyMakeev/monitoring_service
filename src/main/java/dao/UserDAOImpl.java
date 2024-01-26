package dao;

import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserDAOImpl implements DAO<User> {
    private final List<User> users = new ArrayList<>();
    public UserDAOImpl() {
        User admin = new User("admin", "admin", true);
        users.add(admin);
    }

    @Override
    public Optional<User> getByLogin(String login) {
        for (User user : users) {
            if (Objects.equals(user.login(), login)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public void add(User user) {
        users.add(user);
    }
}
