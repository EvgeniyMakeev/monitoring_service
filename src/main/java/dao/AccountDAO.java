package dao;

import model.Account;
import model.User;

public interface AccountDAO {
    Account create(User user, String password);
}
