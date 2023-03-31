package fr.hedwin.objects.aliment;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public enum Nutriment {

    @JsonProperty("energy") ENERGY("energy", "327"),
    @JsonProperty("energy-kj") ENERGY_KJ("energy-kj", "327"),
    @JsonProperty("energy-kcal") ENERGY_KCAL("energy-kcal", "328"),
    @JsonProperty("proteins") PROTEINS("proteins", "25000"),
    @JsonProperty("casein") CASEIN("casein", ""),
    @JsonProperty("erum-proteins") ERUM_PROTEINS("erum-proteins", ""),
    @JsonProperty("nucleotides") NUCLEOTIDES("nucleotides", ""),
    @JsonProperty("carbohydrates") CARBOHYDRATES("carbohydrates", "31000"),
    @JsonProperty("sugars") SUGARS("sugars", "32000"),
    @JsonProperty("sucrose") SUCROSE("sucrose", "32480"),
    @JsonProperty("glucose") GLUCOSE("glucose", "32250"),
    @JsonProperty("fructose") FRUCTOSE("fructose", "32210"),
    @JsonProperty("lactose") LACTOSE("lactose", "32410"),
    @JsonProperty("maltose") MALTOSE("maltose", "32430"),
    @JsonProperty("maltodextrins") MALTODEXTRINS("maltodextrins", ""),
    @JsonProperty("starch") STARCH("starch", "33110"),
    @JsonProperty("polyols") POLYOLS("polyols", "34000"),
    @JsonProperty("fat") FAT("fat", "40000"),
    @JsonProperty("salt") SALT("salt", "10004"),
    @JsonProperty("saturated-fat") SATURATED_FAT("saturated-fat", "40302"),
    @JsonProperty("butyric-acid") BUTYRIC_ACID("butyric-acid", "40400"),
    @JsonProperty("caproic-acid") CAPROIC_ACID("caproic-acid", "40600"),
    @JsonProperty("caprylic-acid") CAPRYLIC_ACID("caprylic-acid", "40800"),
    @JsonProperty("capric-acid") CAPRIC_ACID("capric-acid", "41000"),
    @JsonProperty("lauric-acid") LAURIC_ACID("lauric-acid", "41200"),
    @JsonProperty("myristic-acid") MYRISTIC_ACID("myristic-acid", "41400"),
    @JsonProperty("palmitic-acid") PALMITIC_ACID("palmitic-acid", "41600"),
    @JsonProperty("stearic-acid") STEARIC_ACID("stearic-acid", "41800"),
    @JsonProperty("arachidic-acid") ARACHIDIC_ACID("arachidic-acid", ""),
    @JsonProperty("behenic-acid") BEHENIC_ACID("behenic-acid", ""),
    @JsonProperty("lignoceric-acid") LIGNOCERIC_ACID("lignoceric-acid", ""),
    @JsonProperty("cerotic-acid") CEROTIC_ACID("cerotic-acid", ""),
    @JsonProperty("montanic-acid") MONTANIC_ACID("montanic-acid", ""),
    @JsonProperty("melissic-acid") MELISSIC_ACID("melissic-acid", ""),
    @JsonProperty("monounsaturated-fat") MONOUNSATURATED_FAT("monounsaturated-fat", "40303"),
    @JsonProperty("polyunsaturated-fat") POLYUNSATURATED_FAT("polyunsaturated-fat", "40304"),
    @JsonProperty("omega-3-fat") OMEGA_3_FAT("omega-3-fat", "42053"),
    @JsonProperty("alpha-linolenic-acid") ALPHA_LINOLENIC_ACID("alpha-linolenic-acid", "41833"),
    @JsonProperty("eicosapentaenoic-acid") EICOSAPENTAENOIC_ACID("eicosapentaenoic-acid", ""),
    @JsonProperty("docosahexaenoic-acid") DOCOSAHEXAENOIC_ACID("docosahexaenoic-acid", ""),
    @JsonProperty("omega-6-fat") OMEGA_6_FAT("omega-6-fat", "42263"),
    @JsonProperty("linoleic-acid") LINOLEIC_ACID("linoleic-acid", "41826"),
    @JsonProperty("arachidonic-acid") ARACHIDONIC_ACID("arachidonic-acid", "42046"),
    @JsonProperty("gamma-linolenic-acid") GAMMA_LINOLENIC_ACID("gamma-linolenic-acid", ""),
    @JsonProperty("dihomo-gamma-linolenic-acid") DIHOMO_GAMMA_LINOLENIC_ACID("dihomo-gamma-linolenic-acid", ""),
    @JsonProperty("omega-9-fat") OMEGA_9_FAT("omega-9-fat", ""),
    @JsonProperty("oleic-acid") OLEIC_ACID("oleic-acid", "41819"),
    @JsonProperty("elaidic-acid") ELAIDIC_ACID("elaidic-acid", ""),
    @JsonProperty("gondoic-acid") GONDOIC_ACID("gondoic-acid", ""),
    @JsonProperty("mead-acid") MEAD_ACID("mead-acid", ""),
    @JsonProperty("erucic-acid") ERUCIC_ACID("erucic-acid", ""),
    @JsonProperty("nervonic-acid") NERVONIC_ACID("nervonic-acid", ""),
    @JsonProperty("trans-fat") TRANS_FAT("trans-fat", ""),
    @JsonProperty("cholesterol") CHOLESTEROL("cholesterol", "75100"),
    @JsonProperty("fiber") FIBER("fiber", "34100"),
    @JsonProperty("sodium") SODIUM("sodium", "10110"),
    @JsonProperty("alcohol") ALCOHOL("alcohol", "60000"),
    @JsonProperty("vitamin-a") VITAMIN_A("vitamin-a", "51330"),
    @JsonProperty("retinol") RETINOL("retinol", "51200"),
    @JsonProperty("vitamin-d") VITAMIN_D("vitamin-d", "52100"),
    @JsonProperty("vitamin-e") VITAMIN_E("vitamin-e", "53100"),
    @JsonProperty("vitamin-k") VITAMIN_K("vitamin-k", "54101"),
    @JsonProperty("vitamin-c") VITAMIN_C("vitamin-c", "55100"),
    @JsonProperty("vitamin-b1") VITAMIN_B1("vitamin-b1", "56100"),
    @JsonProperty("vitamin-b2") VITAMIN_B2("vitamin-b2", "56200"),
    @JsonProperty("vitamin-pp") VITAMIN_B3_PP("vitamin-pp", "56310"),
    @JsonProperty("vitamin-b6") VITAMIN_B6("vitamin-b6", "56500"),
    @JsonProperty("vitamin-b9") VITAMIN_B9("vitamin-b9", "56700"),
    @JsonProperty("vitamin-b12") VITAMIN_B12("vitamin-b12", "56600"),
    @JsonProperty("biotin") BIOTIN("biotin", ""),
    @JsonProperty("pantothenic-acid") PANTOTHENIC_ACID_VITAMIN_B5("pantothenic-acid", "56400"),
    @JsonProperty("silica") SILICA("silica", ""),
    @JsonProperty("bicarbonate") BICARBONATE("bicarbonate", ""),
    @JsonProperty("potassium") POTASSIUM("potassium", "10190"),
    @JsonProperty("chloride") CHLORIDE("chloride", "10170"),
    @JsonProperty("calcium") CALCIUM("calcium", "10200"),
    @JsonProperty("phosphorus") PHOSPHORUS("phosphorus", "10150"),
    @JsonProperty("iron") IRON("iron", "10260"),
    @JsonProperty("magnesium") MAGNESIUM("magnesium", "10120"),
    @JsonProperty("zinc") ZINC("zinc", "10300"),
    @JsonProperty("copper") COPPER("copper", "10290"),
    @JsonProperty("manganese") MANGANESE("manganese", "10251"),
    @JsonProperty("fluoride") FLUORIDE("fluoride", ""),
    @JsonProperty("selenium") SELENIUM("selenium", "10340"),
    @JsonProperty("chromium") CHROMIUM("chromium", ""),
    @JsonProperty("molybdenum") MOLYBDENUM("molybdenum", ""),
    @JsonProperty("iodine") IODINE("iodine", "10530"),
    @JsonProperty("caffeine") CAFFEINE("caffeine", ""),
    @JsonProperty("taurine") TAURINE("taurine", ""),
    @JsonProperty("ph") PH("ph", ""),
    @JsonProperty("water") WATER("water", "400"),
    @JsonProperty("fruits-vegetables-nuts") FRUITS_VEGETABLES_NUTS("fruits-vegetables-nuts", ""),
    @JsonProperty("carbon-footprint") CARBON_FOOTPRINT("carbon-footprint", "10000"),
    @JsonProperty("nutrition-score-fr") NUTRITION_SCORE_FR("nutrition-score-fr", ""),
    @JsonProperty("nutrition-score-uk") NUTRITION_SCORE_UK("nutrition-score-uk", "");

    private final String key;
    private final String ciqual_key;

    Nutriment(String key, String ciqual_key){
        this.key = key;
        this.ciqual_key = ciqual_key;
    }
    public String getKey() {
        return key;
    }

    public String getCiqualKey() {
        return ciqual_key;
    }

    public static Nutriment getNutriment(String key){
        String t = key.split("_")[0];
        return Arrays.stream(Nutriment.values()).filter(n -> t.equals(n.getKey())).findFirst().orElse(null);
    }

    public static Nutriment getCiqualNutriment(String ciqual_key){
        return Arrays.stream(Nutriment.values()).filter(n -> ciqual_key.equals(n.getCiqualKey())).findFirst().orElse(null);
    }

    public static void updateNutrimentInfos(Nutriment nutriment, String key, Object value, Infos info){
        if(key.equals(nutriment.key+"_100g")) info.setValue_100g((Double) value);
        //else if(key.equals(nutriment.key+"_serving")) info.setValue_serving((Double) value);
        //else if(key.equals(nutriment.key+"_value")) info.setValue((Double) value);
        else if(key.equals(nutriment.key+"_unit")) info.setUnit((String) value);
        //else if(key.equals(nutriment.key+"_label")) info.setLabel((String) value);
    }

    public static class Infos{
        private double value_100g;
        private String unit;

        public Infos(double value_100g, String unit) {
            this.value_100g = value_100g;
            this.unit = unit;
        }

        public double getValue_100g() {
            return value_100g;
        }

        public void setValue_100g(double value_100g) {
            this.value_100g = value_100g;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String units) {
            this.unit = units;
        }
    }
}
