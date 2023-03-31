package fr.hedwin.sql.tables;

import fr.hedwin.objects.elements.Day;
import fr.hedwin.objects.elements.Repas;
import fr.hedwin.objects.elements.Week;
import fr.hedwin.sql.dao.DaoAdapter;
import fr.hedwin.sql.dao.DaoFactory;
import fr.hedwin.sql.exceptions.DaoException;
import fr.hedwin.sql.utils.Selectable;

import java.util.HashMap;
import java.util.Map;

public class WeekDaoImpl extends DaoAdapter<String, Week> {

    private DaoFactory daoFactory;

    public WeekDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void add(Week week) throws DaoException {
        String id = daoFactory.request("INSERT INTO week (name, id_profil) VALUE (?, ?)", "SELECT last_insert_id() as id_week;", preparedStatement -> {
            preparedStatement.setString(1, week.name()+"");
            preparedStatement.setInt(2, week.getId_profil());
        }, resultat -> {
            if(resultat.next()){
                return resultat.getString("id_week");
            }
            return null;
        });
        if(id == null) return;
        week.setId(id);
    }

    @Override
    public void delete(String id) throws DaoException {
        daoFactory.request("DELETE FROM day_repas WHERE id_week = ?; DELETE FROM week WHERE id_week = ?;", preparedStatement -> {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, id);
        });
    }

    public void addOrUpdateRepas(String id_week, int day, String last_name, String name, int id_repas) throws DaoException {
        daoFactory.request("INSERT INTO day_repas (id_week, day, name, id_repas) VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE id_repas = ?, name = ?;", preparedStatement -> {
            preparedStatement.setInt(1, Integer.parseInt(id_week));
            preparedStatement.setInt(2, day);
            preparedStatement.setString(3, last_name);
            preparedStatement.setInt(4, id_repas);
            preparedStatement.setInt(5, id_repas);
            preparedStatement.setString(6, name);
        });
    }

    public void removeRepas(String id_week, int day, String name) throws DaoException {
        daoFactory.request("DELETE FROM day_repas WHERE id_week = ? and name = ? and day = ?", preparedStatement -> {
            preparedStatement.setInt(1, Integer.parseInt(id_week));
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, day);
        });
    }

    @Override
    public Map<String, Week> select(Selectable selectable) throws DaoException {
        return daoFactory.request("""
                SELECT *, `day_repas`.day, `day_repas`.`name`, `repas`.*
                FROM `week`
                    LEFT JOIN `day_repas` ON `day_repas`.`id_week` = `week`.`id_week`
                    LEFT JOIN `repas` ON `day_repas`.`id_repas` = `repas`.`id_repas`"""+selectable.toString(), resultSet -> {
            Map<String, Week> weeks = new HashMap<>();

            while (resultSet.next()){
                String id_week = resultSet.getString("id_week");
                String week_name = resultSet.getString("week.name");
                int id_profil = resultSet.getInt("id_profil");

                String dayRang = resultSet.getString("day");

                String id_repas = resultSet.getString("id_repas");
                String name = resultSet.getString("day_repas.name");
                String r_name = resultSet.getString("repas.name");
                boolean fav = resultSet.getBoolean("fav");

                if(!weeks.containsKey(id_week)) {
                    Week w = new Week(id_week, week_name, id_profil);
                    if(dayRang != null){
                        Day.DayType dayType = Day.DayType.getDay(Integer.parseInt(dayRang));
                        w.addElement(dayType.getRang()+"", new Day(id_week, dayType, id_profil));
                    }
                    weeks.put(id_week, w);
                }else if(dayRang != null){
                    Day.DayType dayType = Day.DayType.getDay(Integer.parseInt(dayRang));
                    weeks.get(id_week).addElement(dayType.getRang()+"", new Day(id_week, dayType, id_profil));
                }

                if(id_repas != null) {
                    assert dayRang != null; // id_repas est forcement null si dayRang l'est
                    Day.DayType dayType = Day.DayType.getDay(Integer.parseInt(dayRang));
                    weeks.get(id_week).getElements().get(dayType.getRang()+"").addElement(name, new Repas(id_repas, r_name, id_profil, fav));
                }
            }
            return weeks;
        });
    }
}
