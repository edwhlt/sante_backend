package fr.hedwin.objects.elements;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.hedwin.objects.aliment.Nutriment;
import fr.hedwin.objects.modele.Apport;
import fr.hedwin.objects.modele.NotAssociateException;
import fr.hedwin.objects.modele.Quantified;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Recette implements Quantified, Apport {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("guest")
    private int nb_guest;
    @JsonProperty("recette")
    private String recette;
    @JsonProperty("id_profil")
    private int idProfil;

    @JsonProperty("fav")
    private boolean fav;

    @JsonProperty("elements")
    private final Map<String, Ingredient<FoodElement>> elements = new HashMap<>();

    public Recette(String id, String name, int nb_guest, String recette, int idProfil, boolean fav) {
        this.id = id;
        this.name = name;
        this.nb_guest = nb_guest;
        this.recette = recette;
        this.idProfil = idProfil;
        this.fav = fav;
    }

    public Recette(){}

    @JsonGetter("id")
    @Override
    public String key() {
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

    @JsonGetter("recette")
    public String getRecette() {
        return recette;
    }

    @JsonGetter("fav")
    public boolean isFav() {
        return fav;
    }

    @JsonGetter("guest")
    @Override
    public double getQuantity() {
        return nb_guest;
    }

    @Override
    public void setQuantity(double quantity) {
        this.nb_guest = (int) quantity;
    }

    @JsonGetter("id_profil")
    public int getIdProfil() {
        return idProfil;
    }

    @Override
    public double getApport(Nutriment nutriment) throws RuntimeException {
        return elements.values().stream().mapToDouble(e -> {
            try {
                return e.getApport(nutriment);
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

    @JsonGetter("elements")
    public Map<String, Ingredient<FoodElement>> getElements() {
        return elements;
    }

    public void addElement(String key, Ingredient<FoodElement> e){
        elements.putIfAbsent(key, e);
    }


}
