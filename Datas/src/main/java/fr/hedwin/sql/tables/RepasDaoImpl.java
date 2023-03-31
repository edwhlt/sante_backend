package fr.hedwin.sql.tables;

import fr.hedwin.objects.elements.FoodElement;
import fr.hedwin.objects.elements.Ingredient;
import fr.hedwin.objects.elements.Recette;
import fr.hedwin.objects.elements.Repas;
import fr.hedwin.sql.dao.DaoAdapter;
import fr.hedwin.sql.dao.DaoFactory;
import fr.hedwin.sql.exceptions.DaoException;
import fr.hedwin.sql.utils.Selectable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class RepasDaoImpl extends DaoAdapter<String, Repas> {

    private DaoFactory daoFactory;

    public RepasDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void add(Repas repas) throws DaoException {
        String id = daoFactory.request("INSERT INTO repas (`name`, `id_profil`) VALUES (?,?);", "SELECT last_insert_id() as id_repas;", preparedStatement -> {
            preparedStatement.setString(1, repas.name());
            preparedStatement.setInt(2, repas.getIdProfil());
        }, resultat -> {
            if(resultat.next()){
                return resultat.getString("id_repas");
            }
            return null;
        });
        if(id == null) return;
        repas.setId(id);
        if(!repas.getElements().isEmpty()) {
            StringJoiner stringJoiner = new StringJoiner(", ");
            repas.getElements().values().forEach(ing -> stringJoiner.add("(?,?,?)"));
            daoFactory.request("INSERT INTO repas_ingredient (`id_repas`, `id_food`, `quantity`) VALUES "+stringJoiner, preparedStatement -> {
                int i = 1;
                for(Map.Entry<String, Ingredient<FoodElement>> ing : repas.getElements().entrySet()){
                    preparedStatement.setString(i, id);
                    preparedStatement.setString(i+1, ing.getKey());
                    preparedStatement.setDouble(i+2, ing.getValue().getQuantity());
                    i += 3;
                }
            });
        }
        if(!repas.getRecettes().isEmpty()) {
            StringJoiner stringJoiner = new StringJoiner(", ");
            repas.getRecettes().values().forEach(ing -> stringJoiner.add("(?,?,?)"));
            daoFactory.request("INSERT INTO repas_recette (`id_repas`, `id_recette`, `guest`) VALUES "+stringJoiner, preparedStatement -> {
                int i = 1;
                for(Map.Entry<String, Ingredient<Recette>> ing : repas.getRecettes().entrySet()){
                    preparedStatement.setString(i, id);
                    preparedStatement.setString(i+1, ing.getKey());
                    preparedStatement.setDouble(i+2, ing.getValue().getQuantity());
                    i += 3;
                }
            });
        }
    }

    @Override
    public void delete(String id) throws DaoException {
        daoFactory.request("""
            DELETE FROM repas_ingredient WHERE id_repas = ?;
            DELETE FROM repas_recette WHERE id_repas = ?;
            DELETE FROM day_repas WHERE id_repas = ?;
            DELETE FROM repas WHERE id_repas = ?;
            """, preparedStatement -> {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, id);
            preparedStatement.setString(3, id);
            preparedStatement.setString(4, id);
        });
    }

    @Override
    public void update(Repas repas) throws DaoException {
        daoFactory.request("UPDATE repas SET name = ? WHERE id_repas = ?", preparedStatement -> {
            preparedStatement.setString(1, repas.name());
            preparedStatement.setString(2, repas.key());
        });
        if(!repas.getElements().isEmpty()) {
            StringJoiner stringJoiner = new StringJoiner("");
            repas.getElements().forEach((k, ing) -> {
                stringJoiner.add("INSERT INTO repas_ingredient (id_repas, id_food, quantity) VALUES(%s, %s, %f) ON DUPLICATE KEY UPDATE quantity = %f;"
                        .formatted(repas.key(), k, ing.getQuantity(), ing.getQuantity())
                );
            });
            daoFactory.request(stringJoiner.toString());
        }
        if(!repas.getRecettes().isEmpty()){
            StringJoiner stringJoiner = new StringJoiner("");
            repas.getRecettes().forEach((k, ing) -> {
                stringJoiner.add("INSERT INTO repas_recette (id_repas, id_recette, guest) VALUES(%s, %s, %f) ON DUPLICATE KEY UPDATE guest = %f;"
                        .formatted(repas.key(), k, ing.getQuantity(), ing.getQuantity())
                );
            });
            daoFactory.request(stringJoiner.toString());
        }
    }

    public void addIngredient(String id_repas, Ingredient<FoodElement> ing) throws DaoException {
        daoFactory.request("INSERT INTO repas_ingredient (id_repas, id_food, quantity) VALUES (?,?,?)", preparedStatement -> {
            preparedStatement.setString(1, id_repas);
            preparedStatement.setString(2, ing.key());
            preparedStatement.setDouble(3, ing.getQuantity());
        });
    }

    public void removeIngredient(String id_repas, String[] ids_food) throws DaoException {
        String ids = Arrays.stream(ids_food).map(id -> "id_food = "+id).collect(Collectors.joining(" or "));
        daoFactory.request("DELETE FROM repas_ingredient WHERE id_repas = ? and ("+ids+")", preparedStatement -> {
            preparedStatement.setString(1, id_repas);
        });
    }

    public void removeRecette(String id_repas, String[] ids_recette) throws DaoException {
        String ids = Arrays.stream(ids_recette).map(id -> "id_recette = "+id).collect(Collectors.joining(" or "));
        daoFactory.request("DELETE FROM repas_recette WHERE id_repas = ? and ("+ids+")", preparedStatement -> {
            preparedStatement.setString(1, id_repas);
        });
    }

    public void addRecette(String id_repas, Ingredient<Recette> ing) throws DaoException {
        daoFactory.request("INSERT INTO repas_recette (id_repas, id_recette, guest) VALUES (?,?,?)", preparedStatement -> {
            preparedStatement.setString(1, id_repas);
            preparedStatement.setString(2, ing.key());
            preparedStatement.setDouble(3, ing.getQuantity());
        });
    }

    @Override
    public Map<String, Repas> select(Selectable selectable) throws DaoException {
        return daoFactory.request("""
                SELECT * FROM (SELECT repas.id_repas, repas.name as repas_name, repas.fav, repas.id_profil, repas_recette.id_recette as id, repas_recette.guest as quantity, "recette" as type
                FROM repas
                	RIGHT JOIN repas_recette ON repas_recette.id_repas = repas.id_repas
                	LEFT JOIN recette ON repas_recette.id_recette = recette.id_recette
                UNION SELECT repas.id_repas, repas.name as repas_name, repas.fav, repas.id_profil, element_food.id_food as id, repas_ingredient.quantity as quantity, 'food' as type
                FROM repas
                	RIGHT JOIN repas_ingredient ON repas_ingredient.id_repas = repas.id_repas
                	LEFT JOIN element_food ON repas_ingredient.id_food = element_food.id_food) as repas
                """+selectable.toString(), resultat -> {

            Map<String, Repas> repasMap = new HashMap<>();

            while (resultat.next()) {
                String id_repas = resultat.getString("id_repas");
                String repas_name = resultat.getString("repas_name");
                int idProfil = resultat.getInt("id_profil");
                boolean fav = resultat.getBoolean("fav");

                String id = resultat.getString("id");
                double quantity = resultat.getDouble("quantity");
                String type = resultat.getString("type");

                if(!repasMap.containsKey(id_repas)){
                    Repas repas = new Repas(id_repas, repas_name, idProfil, fav);
                    repasMap.put(id_repas, repas);
                }

                if(id != null){
                    if(type.equals("food")) repasMap.get(id_repas).addElement(id, new Ingredient<>(quantity));
                    else if(type.equals("recette")) repasMap.get(id_repas).addRecette(id, new Ingredient<>(quantity));
                }
            }

            return repasMap;
        });
    }

}
