package dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    Optional<T> getByLogin(String login);
    List<T> getAll();
    void add(T t);
}
