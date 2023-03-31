package fr.hedwin.objects;

import fr.hedwin.objects.modele.Unitable;

import java.util.HashMap;
import java.util.Map;

public class CiqualIngredient {

    public static class Nutriment {
        private double quantity;
        private String unit;

        public Nutriment(double quantity, String unit) {
            this.quantity = quantity;
            this.unit = unit;
        }

        public double getQuantity() {
            return quantity;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }

    private String code;
    private String name;
    private Map<String, Nutriment> nutrimentMap = new HashMap<>();
    private Unitable.Unit unit;

    public CiqualIngredient(String code, String name, Unitable.Unit unit) {
        this.code = code;
        this.name = name;
        this.unit = unit;
    }

    public CiqualIngredient(String code, String name, Unitable.Unit unit, Map<String, Nutriment> nutrimentMap){
        this(code, name, unit);
        this.nutrimentMap = nutrimentMap;
    }

    public void addNutriment(String code, double value, String unit){
        nutrimentMap.put(code, new Nutriment(value, unit));
    }

    public void setUnit(String code, String unit){
        nutrimentMap.get(code).setUnit(unit);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Unitable.Unit getUnit() {
        return unit;
    }

    public void setUnit(Unitable.Unit unit) {
        this.unit = unit;
    }

    public Map<String, Nutriment> getNutrimentMap() {
        return nutrimentMap;
    }

}
