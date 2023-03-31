package fr.hedwin.sql.tables;

import fr.hedwin.objects.CiqualIngredient;
import fr.hedwin.objects.modele.Unitable;
import fr.hedwin.sql.dao.DaoAdapter;
import fr.hedwin.sql.dao.DaoFactory;
import fr.hedwin.sql.exceptions.DaoException;
import fr.hedwin.sql.utils.Selectable;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class CiqualIngredientDaoImpl extends DaoAdapter<String, CiqualIngredient> {

    private DaoFactory daoFactory;

    public CiqualIngredientDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void add(Map<String, CiqualIngredient> map) throws DaoException {
        StringJoiner stringJoiner = new StringJoiner(", ");
        for(int i = 0; i < map.size(); i++) stringJoiner.add("(?,?)");

        daoFactory.request("INSERT INTO `ciq_ingredient` (`code`, `name`) VALUES "+stringJoiner, preparedStatement -> {
            int i = 0;
            for(CiqualIngredient ciqualIngredient : map.values()){
                preparedStatement.setString(i+1, ciqualIngredient.getCode());
                preparedStatement.setString(i+2, ciqualIngredient.getName());
                i += 2;
            }
        });

        int size = map.values().stream().mapToInt(c -> c.getNutrimentMap().size()).sum();
        StringJoiner sj = new StringJoiner(", ");
        for(int i = 0; i < size; i++) sj.add("(?,?,?)");
        daoFactory.request("INSERT INTO ciq_properties (`code_nutriment`, `code`, `quantity`) VALUES "+ sj, preparedStatement -> {
            int i = 0;
            for(CiqualIngredient ciqualIngredient : map.values()){
                for(Map.Entry<String, CiqualIngredient.Nutriment> entry : ciqualIngredient.getNutrimentMap().entrySet()){
                    preparedStatement.setString(i+1, entry.getKey());
                    preparedStatement.setString(i+2, ciqualIngredient.getCode());
                    preparedStatement.setDouble(i+3, entry.getValue().getQuantity());
                    i += 3;
                }
            }
        });
    }

    @Override
    public Map<String, CiqualIngredient> select(Selectable selectable) throws DaoException {
        return daoFactory.request("""
                SELECT food.ciq_ingredient.code, food.ciq_ingredient.name, food.ciq_properties.code_nutriment, food.ciq_properties.quantity
                FROM food.ciq_ingredient
                	LEFT JOIN ciq_properties ON ciq_properties.code = ciq_ingredient.code
                """+selectable.toString(), resultat -> {
            Map<String, CiqualIngredient> ciqualIngredientMap = new HashMap<>();
            while (resultat.next()) {
                String code = resultat.getString("code");
                String name = resultat.getString("name");

                String code_nutriment = resultat.getString("code_nutriment");
                double quantity = resultat.getDouble("quantity");

                if(ciqualIngredientMap.containsKey(code)){
                    ciqualIngredientMap.get(code).addNutriment(code_nutriment, quantity, "g");
                }else{
                    Map<String, CiqualIngredient.Nutriment> nutrimentMap = new HashMap<>();
                    CiqualIngredient ciqualIngredient = new CiqualIngredient(code, name, Unitable.Unit.GRAMME);
                    ciqualIngredient.addNutriment(code_nutriment, quantity, "g");
                    ciqualIngredientMap.put(code, ciqualIngredient);
                }
            }
            return ciqualIngredientMap;
        });
    }

    @Override
    public void truncate() throws DaoException {
        daoFactory.request("""
                SET FOREIGN_KEY_CHECKS = 0;
                TRUNCATE ciq_properties;
                TRUNCATE ciq_ingredient;
                SET FOREIGN_KEY_CHECKS = 1;
                """, preparedStatement -> {
        });
    }
}

