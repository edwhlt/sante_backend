package fr.hedwin.sql;

import fr.hedwin.Actions;
import fr.hedwin.object.json.exceptions.NotFoundElement;
import fr.hedwin.objects.ApiUser;
import fr.hedwin.objects.CiqualIngredient;
import fr.hedwin.objects.Profil;
import fr.hedwin.objects.aliment.Aliment;
import fr.hedwin.objects.elements.*;
import fr.hedwin.objects.manage.RecetteMap;
import fr.hedwin.objects.manage.RepasMap;
import fr.hedwin.sql.dao.DaoFactory;
import fr.hedwin.sql.exceptions.DaoException;
import fr.hedwin.sql.utils.Selectable;

import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class DATA {

    public static Map<String, Aliment> memories = new HashMap<>();

    public static final DaoFactory daoFactory = DaoFactory.getInstance();

    public static Aliment getAliment(String code) throws NotFoundElement {
        if(memories.containsKey(code)) return memories.get(code);
        try {
            Map<String, CiqualIngredient> ciqualIngredientMap = daoFactory.getCiqualIngredientDao().select(new Selectable(new String[]{"ciq_ingredient.code", code}));
            if(ciqualIngredientMap.containsKey(code)) {
                Aliment a = Actions.generateAlimentFromCiqual(ciqualIngredientMap.get(code));
                memories.put(code, a);
                return a;
            }
        }catch (DaoException ignored){}
        System.out.println("Appel OFF: "+code);
        Aliment a = Actions.searchAliment(code);
        memories.put(code, a);
        return a;
    }

    public static Map<String, FoodElement> getFoodElements() throws DaoException {
        return daoFactory.getFoodElementDao().select(new Selectable());
    }

    public static RecetteMap getRecette(int idProfil) throws DaoException {
        Map<String, Recette> map = daoFactory.getRecetteDao().select(new Selectable(new Object[]{"recette.id_profil", idProfil}));
        return new RecetteMap(map);
    }

    public static void addRecette(Recette recette) throws DaoException {
        daoFactory.getRecetteDao().add(recette);
    }
    public static void removeRecette(String id_recette) throws DaoException {
        daoFactory.getRecetteDao().delete(id_recette);
    }

    public static RecetteMap getRecetteWithNutriment(int idProfil) throws DaoException {
        RecetteMap recetteMap = getRecette(idProfil);

        recetteMap.updateElements((k, v) -> {
            FoodElement foodElement = v.getElement();
            try {
                foodElement.setElement(getAliment(foodElement.getCode()));
            } catch (NotFoundElement notFoundElement) {
                notFoundElement.printStackTrace();
            }
        });
        return recetteMap;
    }

    public static RepasMap getRepas(int idProfil) throws DaoException {
        Map<String, Repas> map = daoFactory.getRepasDao().select(new Selectable(new Object[]{"repas.id_profil", idProfil}));
        RepasMap repasMap = new RepasMap(map);
        Map<String, FoodElement> foodElement = getFoodElements();
        RecetteMap recetteMap = getRecette(idProfil);

        repasMap.updateElements((k, v) -> v.setElement(foodElement.get(k)));
        repasMap.updateRecettes((k, r) -> r.setElement(recetteMap.get(k)));

        return repasMap;
    }
    public static void addRepas(Repas repas) throws DaoException {
        daoFactory.getRepasDao().add(repas);
    }
    public static void removeRepas(String id_repas) throws DaoException {
        daoFactory.getRepasDao().delete(id_repas);
    }

    public static RepasMap getRepasWithNutriment(int idProfil) throws DaoException {
        RepasMap repasMap = getRepas(idProfil);

        BiConsumer<String, FoodElement> consumer = (k, foodElement) -> {
            try {
                foodElement.setElement(getAliment(foodElement.getCode()));
            } catch (NotFoundElement notFoundElement) {
                notFoundElement.printStackTrace();
            }
        };

        repasMap.updateElements((k, v) -> consumer.accept(k, v.getElement()));
        repasMap.updateRecettes((k, r) -> {
            r.getElement().updateElements((key, v) -> consumer.accept(key, v.getElement()));
        });

        return repasMap;
    }

    public static Map<String, Week> getWeeks(int idProfil) throws DaoException {
        return daoFactory.getWeekDao().select(new Selectable(new Object[]{"week.id_profil", idProfil}));
    }

    public static Map<String, Week> getWeeksWithNutriments(int idProfil) throws DaoException {
        Map<String, Week> weeks = getWeeks(idProfil);
        RepasMap repasMap = getRepasWithNutriment(idProfil);

        weeks.forEach((s, w) -> {
           w.getElements().forEach((k, day) -> {
               day.getElements().forEach((key, repas) -> {
                   day.updateElement(key, repasMap.get(repas.key()));
               });
           });
        });
        return weeks;
    }

    public static Week getWeek(int idProfil, String id_week) throws DaoException {
        return daoFactory.getWeekDao().select(new Selectable(new Object[]{"week.id_week", id_week})).get(id_week);
    }

    public static Week getWeekWithNutriments(int idProfil, String id_week) throws DaoException {
        Week w = getWeek(idProfil, id_week);
        RepasMap repasMap = getRepasWithNutriment(idProfil);

        w.getElements().forEach((k, day) -> {
            day.getElements().forEach((key, repas) -> {
                day.updateElement(key, repasMap.get(repas.key()));
            });
        });
        return w;
    }

    public static Map<Integer, Profil> getUserProfils(int idUser) throws DaoException {
        return daoFactory.getProfilDao().select(new Selectable(new Object[]{"profil.id_user", idUser}));
    }

    public static Profil getProfil(int idProfil) throws DaoException {
        return daoFactory.getProfilDao().select(new Selectable(new Object[]{"profil.id_profil", idProfil})).get(idProfil);
    }

    public static void deleteProfil(int idProfil) throws DaoException {
        daoFactory.getProfilDao().delete(idProfil);
    }

    public static void addProfil(int id_user, String name, double weight, double size, int age, int genre, double activity, double factor) throws DaoException {
        Profil profil = new Profil(-1, name, weight, size, age, genre, activity, factor, 0.5, 0.2, 0.3, id_user);
        daoFactory.getProfilDao().add(profil);
    }

    public static Map<Integer, ApiUser> getApiAccess() throws DaoException {
        return daoFactory.getUserDao().getApiUser(new Selectable());
    }

}
