package fr.hedwin.sql.tables;

import fr.hedwin.objects.CiqualIngredient;
import fr.hedwin.objects.elements.FoodElement;
import fr.hedwin.sql.dao.DaoAdapter;
import fr.hedwin.sql.dao.DaoFactory;
import fr.hedwin.sql.exceptions.DaoException;
import fr.hedwin.sql.utils.Selectable;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class FoodElementDaoImpl extends DaoAdapter<String, FoodElement> {

    private DaoFactory daoFactory;

    public FoodElementDaoImpl(DaoFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    @Override
    public void add(Map<String, FoodElement> map) throws DaoException {
        StringJoiner stringJoiner = new StringJoiner(", ");
        for(int i = 0; i < map.size(); i++) stringJoiner.add("(?,?,?)");

        daoFactory.request("INSERT INTO element_food(code, name, unit) VALUES "+stringJoiner, preparedStatement -> {
            int i = 0;
            for(FoodElement foodElement : map.values()){
                preparedStatement.setString(i+1, foodElement.getCode());
                preparedStatement.setString(i+2, foodElement.name());
                preparedStatement.setString(i+3, foodElement.getUnit().getUnit());
                i += 3;
            }
        });
    }

    @Override
    public void add(FoodElement foodElement) throws DaoException {
        String id = daoFactory.request("INSERT INTO element_food(code, name, unit) VALUES (?,?,?);", "SELECT last_insert_id() as id_food;", preparedStatement -> {
            preparedStatement.setString(1, foodElement.getCode());
            preparedStatement.setString(2, foodElement.name());
            preparedStatement.setString(3, foodElement.getUnit().getUnit());
        }, resultat -> {
            if(resultat.next()){
                return resultat.getString("id_food");
            }
            return null;
        });
        if(id == null) return;
        foodElement.setId(id);
    }

    @Override
    public Map<String, FoodElement> select(Selectable selectable) throws DaoException {
        return daoFactory.request("""
                SELECT * from element_food
                """+selectable.toString(), resultat -> {
           Map<String, FoodElement> map = new HashMap<>();

           while(resultat.next()){
               String id = resultat.getString("id_food");
               String code = resultat.getString("code");
               String name = resultat.getString("name");
               String unit = resultat.getString("unit");

               map.put(id, new FoodElement(id, code, name, unit));
           }
           return map;
        });
    }
}
