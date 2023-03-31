package fr.hedwin.sql.tables;

import fr.hedwin.objects.Profil;
import fr.hedwin.sql.dao.DaoAdapter;
import fr.hedwin.sql.dao.DaoFactory;
import fr.hedwin.sql.exceptions.DaoException;
import fr.hedwin.sql.utils.Selectable;

import java.util.HashMap;
import java.util.Map;

public class ProfilDaoImpl extends DaoAdapter<Integer, Profil> {

    private DaoFactory daoFactory;

    public ProfilDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void delete(Integer id) throws DaoException {
        daoFactory.request("""
            DELETE FROM profil WHERE (id_profil = ?)
            """, preparedStatement -> {
            preparedStatement.setInt(1, id);
        });
    }

    @Override
    public void add(Profil profil) throws DaoException {
        int id = daoFactory.request(
                "INSERT INTO `food`.`profil` (`name`, `weight`, `size`, `age`, `physical_activity`, `factor`, `carbohydrate`, `protein`, `lipid`, `genre`, `id_user`) VALUES (?,?,?,?,?,?,?,?,?,?,?);",
                "SELECT last_insert_id() as id_profil;", preparedStatement -> {
            preparedStatement.setString(1, profil.getName());
            preparedStatement.setDouble(2, profil.getWeight());
            preparedStatement.setDouble(3, profil.getSize());
            preparedStatement.setInt(4, profil.getAge());
            preparedStatement.setDouble(5, profil.getPhysical_activity());
            preparedStatement.setDouble(6, profil.getFactor());
            preparedStatement.setInt(7, (int) (profil.getCarbohydrate()*100));
            preparedStatement.setInt(8, (int) (profil.getProtein()*100));
            preparedStatement.setInt(9, (int) (profil.getLipid()*100));
            preparedStatement.setInt(10, profil.getSexe());
            preparedStatement.setInt(11, profil.getId_user());
        }, resultat -> {
            if(resultat.next()){
                return resultat.getInt("id_profil");
            }
            return null;
        });
        profil.setId(id);
    }

    @Override
    public void update(Profil profil) throws DaoException {
        daoFactory.request("UPDATE food.profil SET name = ?, weight = ?, size = ?, age = ?, physical_activity = ?, factor = ?, carbohydrate = ?, protein = ?, lipid = ?, genre = ? WHERE id_profil = ?", preparedStatement -> {
            preparedStatement.setString(1, profil.getName());
            preparedStatement.setDouble(2, profil.getWeight());
            preparedStatement.setDouble(3, profil.getSize());
            preparedStatement.setInt(4, profil.getAge());
            preparedStatement.setDouble(5, profil.getPhysical_activity());
            preparedStatement.setDouble(6, profil.getFactor());
            preparedStatement.setInt(7, (int) (profil.getCarbohydrate()*100));
            preparedStatement.setInt(8, (int) (profil.getProtein()*100));
            preparedStatement.setInt(9, (int) (profil.getLipid()*100));
            preparedStatement.setInt(10, profil.getSexe());
            preparedStatement.setInt(11, profil.getId());
        });
    }

    @Override
    public Map<Integer, Profil> select(Selectable selectable) throws DaoException {
        return daoFactory.request("SELECT * FROM profil "+selectable.toString(), resultat -> {
            Map<Integer, Profil> profils = new HashMap<>();
            while (resultat.next()) {
                int id = resultat.getInt("id_profil");
                String name = resultat.getString("name");
                double weight = resultat.getDouble("weight");
                double size = resultat.getDouble("size");
                int age = resultat.getInt("age");
                int sexe = resultat.getInt("genre");
                double physical_activity = resultat.getDouble("physical_activity");
                double factor = resultat.getDouble("factor");
                double carbohydrate = resultat.getDouble("carbohydrate")/100;
                double protein = resultat.getDouble("protein")/100;
                double lipid = resultat.getDouble("lipid")/100;
                int id_user = resultat.getInt("id_user");

                Profil profil = new Profil(id, name, weight, size, age, sexe, physical_activity, factor, carbohydrate, protein, lipid, id_user);
                profils.put(id, profil);
            }
            return profils;
        });
    }

}
