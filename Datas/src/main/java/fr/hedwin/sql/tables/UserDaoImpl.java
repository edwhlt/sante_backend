package fr.hedwin.sql.tables;

import fr.hedwin.objects.ApiUser;
import fr.hedwin.objects.User;
import fr.hedwin.sql.dao.DaoAdapter;
import fr.hedwin.sql.dao.DaoFactory;
import fr.hedwin.sql.exceptions.DaoException;
import fr.hedwin.sql.utils.Selectable;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserDaoImpl extends DaoAdapter<Integer, User> {

    private DaoFactory daoFactory;

    public UserDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void add(User user) throws DaoException {
        daoFactory.request("INSERT INTO user (name, email, password, date) VALUES (?,?,?,?)", preparedStatement -> {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setTimestamp(4, new Timestamp(user.getDate().getTime()));
        });
    }

    @Override
    public Map<Integer, User> select(Selectable selectable) throws DaoException {
        return daoFactory.request("SELECT * from user "+selectable.toString(), resultat -> {
            Map<Integer, User> users = new HashMap<>();

            while (resultat.next()){
                int id_user = resultat.getInt("id_user");
                String name = resultat.getString("name");
                String email = resultat.getString("email");
                String password = resultat.getString("password");
                Date date = resultat.getTimestamp("date");

                User user = new User(id_user, name, email, password, date);
                users.put(id_user, user);
            }
            return users;
        });
    }

    public Map<Integer, ApiUser> getApiUser(Selectable selectable) throws DaoException {
        return daoFactory.request("SELECT * from api_access "+selectable.toString(), resultat -> {
            Map<Integer, ApiUser> api_users = new HashMap<>();

            while (resultat.next()){
                int id = resultat.getInt("id");
                String api_key = resultat.getString("api_key");
                int limit_request = resultat.getInt("limit_request");
                String address = resultat.getString("address");

                ApiUser user = new ApiUser(id, api_key, limit_request, address);
                api_users.put(id, user);
            }
            return api_users;
        });
    }

}
