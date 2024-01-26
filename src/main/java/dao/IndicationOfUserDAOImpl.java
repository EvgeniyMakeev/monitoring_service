package dao;

import model.IndicationOfUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class IndicationOfUserDAOImpl implements DAO<IndicationOfUser> {
    private final List<IndicationOfUser> indicationOfUserList = new ArrayList<>();

    @Override
    public Optional<IndicationOfUser> getByLogin(String login) {
        for (IndicationOfUser indicationOfUser : indicationOfUserList) {
            if (Objects.equals(indicationOfUser.user().login(), login)) {
                return Optional.of(indicationOfUser);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<IndicationOfUser> getAll() {
        return indicationOfUserList;
    }

    public void add(IndicationOfUser indicationOfUser){
        indicationOfUserList.add(indicationOfUser);
    }
}
