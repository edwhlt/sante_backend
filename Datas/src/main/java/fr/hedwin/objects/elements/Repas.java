package fr.hedwin.objects.elements;

import com.fasterxml.jackson.annotation.*;
import fr.hedwin.objects.aliment.Nutriment;
import fr.hedwin.objects.modele.Apport;
import fr.hedwin.objects.modele.NotAssociateException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Repas implements Apport {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("id_profil")
    private int idProfil;

    @JsonProperty("fav")
    private boolean fav;

    @JsonProperty("elements")
    private final Map<String, Ingredient<FoodElement>> elements = new HashMap<>();

    @JsonProperty("recettes")
    private final Map<String, Ingredient<Recette>> recettes = new HashMap<>();

    public Repas(String id, String name, int idProfil, boolean fav) {
        this.id = id;
        this.name = name;
        this.idProfil = idProfil;
        this.fav = fav;
    }

    public Repas(){}

    @JsonGetter("id")
    @Override
    public String key(){
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonGetter("name")
    @Override
    public String name() {
        return name;
    }

    @JsonGetter("id_profil")
    public int getIdProfil() {
        return idProfil;
    }

    @JsonGetter("fav")
    public boolean isFav() {
        return fav;
    }

    @Override
    public double getApport(Nutriment nutriment) throws RuntimeException {
        List<Map<String, ? extends Apport>> list = Arrays.asList(elements, recettes);

        return list.stream().flatMap(e -> e.entrySet().stream()).distinct().mapToDouble(e -> {
            try {
                return e.getValue().getApport(nutriment);
            } catch (NotAssociateException notAssociateException) {
                throw new RuntimeException(notAssociateException);
            }
        }).sum();
    }

    @JsonGetter("nutriments")
    public Map<Nutriment, Double> getNutriments() throws RuntimeException {
        return Arrays.stream(Nutriment.values()).collect(Collectors.toMap(n -> n, this::getApport));
    }

    public final void updateElements(BiConsumer<String, Ingredient<FoodElement>> consumer){
        elements.forEach(consumer);
    }

    public final void updateRecettes(BiConsumer<String, Ingredient<Recette>> consumer){
        recettes.forEach(consumer);
    }

    @JsonGetter("elements")
    public Map<String, Ingredient<FoodElement>> getElements() {
        return elements;
    }

    @JsonGetter("recettes")
    public Map<String, Ingredient<Recette>> getRecettes() {
        return recettes;
    }

    public void addElement(String key, Ingredient<FoodElement> e){
        elements.putIfAbsent(key, e);
    }

    public void addRecette(String key, Ingredient<Recette> e){
        recettes.putIfAbsent(key, e);
    }

    @JsonIgnore
    public Map<String, Ingredient<FoodElement>> getFoodIngredients(){
        Map<String, Ingredient<FoodElement>> foods = new HashMap<>(getElements());
        getRecettes().forEach((k, r) -> {
            Recette recette = r.getElement();
            recette.getElements().forEach((key, i) -> {
                if(foods.containsKey(key)) {
                    Ingredient<FoodElement> lastIng = foods.get(key);
                    lastIng.setQuantity(lastIng.getQuantity() + i.getQuantity() * r.proportion());
                }else{
                    try {
                        Ingredient<FoodElement> ing = i.clone();
                        ing.setQuantity(i.getQuantity() * r.proportion());
                        foods.put(key, ing);
                    } catch (CloneNotSupportedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        });
        return foods;
    }

}
