package dao;

import model.Account;
import model.User;

public class AccountDAOImpl implements AccountDAO {
    @Override
    public Account create(User user, String password) {
        return new Account(user, password);
    }
}
