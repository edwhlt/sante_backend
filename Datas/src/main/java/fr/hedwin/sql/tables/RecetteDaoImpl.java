package fr.hedwin.sql.tables;

import fr.hedwin.objects.elements.FoodElement;
import fr.hedwin.objects.elements.Ingredient;
import fr.hedwin.objects.elements.Recette;
import fr.hedwin.sql.dao.DaoAdapter;
import fr.hedwin.sql.dao.DaoFactory;
import fr.hedwin.sql.exceptions.DaoException;
import fr.hedwin.sql.utils.Selectable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class RecetteDaoImpl extends DaoAdapter<String, Recette> {

    private DaoFactory daoFactory;

    public RecetteDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void add(Recette recette) throws DaoException {
        String id = daoFactory.request("INSERT INTO recette (`name`, `guest`, `recette`, `id_profil`) VALUES (?,?,?,?);", "SELECT last_insert_id() as id_recette;", preparedStatement -> {
            preparedStatement.setString(1, recette.name());
            preparedStatement.setInt(2, (int)recette.getQuantity());
            preparedStatement.setString(3, recette.getRecette());
            preparedStatement.setInt(4, recette.getIdProfil());
        }, resultat -> {
            if(resultat.next()){
                return resultat.getString("id_recette");
            }
            return null;
        });
        if(id == null) return;
        recette.setId(id);
        StringJoiner stringJoiner = new StringJoiner(", ");
        recette.getElements().values().forEach(ing -> stringJoiner.add("(?,?,?)"));
        if(!recette.getElements().isEmpty()) daoFactory.request("INSERT INTO recette_ingredient (`id_recette`, `id_food`, `quantity`) VALUES "+stringJoiner, preparedStatement -> {
            int i = 1;
            for(Map.Entry<String, Ingredient<FoodElement>> ing : recette.getElements().entrySet()){
                preparedStatement.setString(i, id);
                preparedStatement.setString(i+1, ing.getKey());
                preparedStatement.setDouble(i+2, ing.getValue().getQuantity());
                i += 3;
            }
        });
    }

    @Override
    public void delete(String id) throws DaoException {
        daoFactory.request("""
            DELETE FROM recette_ingredient WHERE id_recette = ?;
            DELETE FROM repas_recette WHERE id_recette = ?;
            DELETE FROM recette WHERE id_recette = ?;
            """, preparedStatement -> {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, id);
            preparedStatement.setString(3, id);
        });
    }

    @Override
    public void update(Recette recette) throws DaoException {
        daoFactory.request("UPDATE recette SET name = ?, guest = ?, recette = ? WHERE id_recette = ?", preparedStatement -> {
            preparedStatement.setString(1, recette.name());
            preparedStatement.setInt(2, (int) recette.getQuantity());
            preparedStatement.setString(3, recette.getRecette());
            preparedStatement.setString(4, recette.key());
        });
        StringJoiner stringJoiner = new StringJoiner("");
        recette.getElements().forEach((k, ing) -> {
            stringJoiner.add("INSERT INTO recette_ingredient (id_recette, id_food, quantity) VALUES(%s, %s, %f) ON DUPLICATE KEY UPDATE quantity = %f;"
                    .formatted(recette.key(), k, ing.getQuantity(), ing.getQuantity())
            );
        });
        if(recette.getElements().size() > 0) daoFactory.request(stringJoiner.toString(), preparedStatement -> {});
    }

    public void addIngredient(String id_recette, Ingredient<FoodElement> ing) throws DaoException {
        daoFactory.request("INSERT INTO recette_ingredient (id_recette, id_food, quantity) VALUES (?,?,?)", preparedStatement -> {
            preparedStatement.setString(1, id_recette);
            preparedStatement.setString(2, ing.key());
            preparedStatement.setDouble(3, ing.getQuantity());
        });
    }

    public void removeIngredient(String id_recette, String[] ids_food) throws DaoException {
        String ids = Arrays.stream(ids_food).map(id -> "id_food = "+id).collect(Collectors.joining(" or "));
        daoFactory.request("DELETE FROM recette_ingredient WHERE id_recette = ? and ("+ids+")", preparedStatement -> {
            preparedStatement.setString(1, id_recette);
        });
    }

    @Override
    public Map<String, Recette> select(Selectable selectable) throws DaoException {
        return daoFactory.request("""
                SELECT `recette`.`id_recette`, `recette`.`id_profil`, `recette`.`name` as rname, recette.fav, `recette`.`guest`, `recette`.`recette`, `recette_ingredient`.`id_food`,`recette_ingredient`.`quantity`, `element_food`.`name`, `element_food`.`code`, element_food.unit
                FROM `recette`
                	LEFT JOIN `recette_ingredient` ON `recette_ingredient`.`id_recette` = `recette`.`id_recette`
                	LEFT JOIN `element_food` ON `recette_ingredient`.`id_food` = `element_food`.`id_food`
                """+selectable.toString(), resultSet -> {
            Map<String, Recette> recetteMap = new HashMap<>();

            while(resultSet.next()){
                String id_recette = resultSet.getString("id_recette");
                String rname = resultSet.getString("rname");
                boolean fav = resultSet.getBoolean("fav");
                int nb_guest = resultSet.getInt("guest");

                String id_food = resultSet.getString("id_food");
                String code = resultSet.getString("code");
                String name = resultSet.getString("name");
                double quantity = resultSet.getDouble("quantity");
                String unit = resultSet.getString("unit");
                String rec = resultSet.getString("recette");
                int id_profil = resultSet.getInt("id_profil");

                if(!recetteMap.containsKey(id_recette)){
                    Recette recette = new Recette(id_recette, rname, nb_guest, rec, id_profil, fav);
                    recetteMap.put(id_recette, recette);
                }

                if(id_food != null) {
                    recetteMap.get(id_recette).addElement(id_food, new Ingredient<>(quantity, new FoodElement(id_food, code, name, unit)));
                }
            }
            return recetteMap;
        });
    }

}
