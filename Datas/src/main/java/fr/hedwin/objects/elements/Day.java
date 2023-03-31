package fr.hedwin.objects.elements;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.hedwin.objects.modele.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Day extends Component<Repas> {

    private final String id_week;
    private final DayType dayType;
    private final int idProfil;

    public Day(String id_week, DayType dayType, int idProfil) {
        this.id_week = id_week;
        this.dayType = dayType;
        this.idProfil = idProfil;
    }

    public enum DayType{
        @JsonProperty("0") MONDAY(0, "Lundi"), @JsonProperty("1") THUESDAY(1, "Mardi"), @JsonProperty("2") WEDNESDAY(2, "Mercredi"),
        @JsonProperty("3") THURSDAY(3, "Jeudi"), @JsonProperty("4") FRIDAY(4, "Vendredi"), @JsonProperty("5") SATURDAY(5, "Samedi"),
        @JsonProperty("6") SUNDAY(6, "Dimanche");
        private final int rang;
        private final String value;
        DayType(int rang, String value){
            this.rang = rang;
            this.value = value;
        }
        public String getValue() {
            return value;
        }
        public int getRang() {
            return rang;
        }
        public static DayType getDay(int rang){
            return Arrays.stream(DayType.values()).filter(d -> d.getRang() == rang).findAny().orElse(null);
        }
    }


    @JsonIgnore
    @JsonGetter("id")
    @Override
    public String key() {
        return id_week+"-"+dayType.getRang();
    }

    @JsonIgnore
    @Override
    public String name() {
        return "S"+id_week+", "+dayType.getValue();
    }

    @JsonGetter("id_profil")
    public int getIdProfil() {
        return idProfil;
    }

    @JsonGetter("id_week")
    public String getIdWeek() {
        return id_week;
    }

    @JsonGetter("day_type")
    public DayType getDayType() {
        return dayType;
    }

    @JsonIgnore
    public Map<String, Ingredient<FoodElement>> getFoodIngredients(){
        Map<String, Ingredient<FoodElement>> foods = new HashMap<>();
        getElements().forEach((k, r) -> {
            r.getFoodIngredients().forEach((key, ing) -> {
                if(foods.containsKey(key)) {
                    Ingredient<FoodElement> lastIng = foods.get(key);
                    lastIng.setQuantity(lastIng.getQuantity() + ing.getQuantity());
                }else foods.put(key, ing);
            });
        });
        return foods;
    }

}
