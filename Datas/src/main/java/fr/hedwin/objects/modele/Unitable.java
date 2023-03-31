package fr.hedwin.objects.modele;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public interface Unitable {

    enum Unit{
        @JsonProperty("g") GRAMME("g"), @JsonProperty("mL") MILILITRE("mL"), @JsonProperty("u") UNITE("u"),
        @JsonProperty("cas") CUILLERE_A_SOUPE("cas"), @JsonProperty("cac") CUILLERE_A_CAFE("cac"), @JsonProperty("guest") GUEST("guest");
        private final String unit;
        Unit(String unit){this.unit = unit;}
        public static Unit getUnit(String unit, Unitable element){
            if(unit.startsWith("u(")){
                double v = Double.parseDouble(unit.substring(2, unit.length()-2));
                element.setWeightUnity(v);
                return Unit.UNITE;
            }
            return Arrays.stream(Unit.values()).filter(t -> t.unit.equals(unit)).findFirst().orElse(null);
        }

        public String getUnit() {
            return unit;
        }
    }

    Unit getUnit();
    void setUnit(Unit unit);

    default double weightUnity(){
        return Double.NaN;
    }

    void setWeightUnity(double weightUnity);

}
