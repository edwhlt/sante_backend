package fr.hedwin.objects;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Profil {

    public static int ENERGY_CARBOHYDRATE = 4;
    public static int ENERGY_PROTEIN = 4;
    public static int ENERGY_LIPID = 9;

    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("weight")
    private double weight;
    @JsonProperty("size")
    private double size;
    @JsonProperty("age")
    private int age;
    @JsonProperty("sexe")
    private int sexe;
    @JsonProperty("physical_activity")
    private double physical_activity;
    @JsonProperty("factor")
    private double factor;
    @JsonProperty("carbohydrate")
    private double carbohydrate;
    @JsonProperty("protein")
    private double protein;
    @JsonProperty("lipid")
    private double lipid;
    @JsonProperty("id_user")
    private int id_user;

    public Profil(int id, String name, double weight, double size, int age, int sexe, double physical_activity, double factor, double carbohydrate, double protein, double lipid, int id_user) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.size = size;
        this.age = age;
        this.sexe = sexe;
        this.physical_activity = physical_activity;
        this.factor = factor;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.lipid = lipid;
        this.id_user = id_user;
    }

    public Profil(){}

    public double getEnergyPerDay(){
        if(sexe == 1) return this.physical_activity*(9.5634*this.weight+184.96*this.size/100-4.6756*this.age+655.955)*this.factor;
        return this.physical_activity*(13.7516*this.weight+500.33*this.size/100-6.755*this.age+66.473)*this.factor;
    }

    public double getCarbohydratePerDay(){
        return this.carbohydrate*getEnergyPerDay()/ENERGY_CARBOHYDRATE;
    }

    public double getProteinPerDay(){
        return this.protein*getEnergyPerDay()/ENERGY_PROTEIN;
    }

    public double getLipidPerDay(){
        return this.lipid*getEnergyPerDay()/ENERGY_LIPID;
    }

    public String getStringCarbohydratePerDay(){
        return Math.round(getCarbohydratePerDay()*100)/100.+" ("+getCarbohydrate()*100+"%)";
    }

    public String getStringProteinPerDay(){
        return Math.round(getProteinPerDay()*100)/100.+" ("+getProtein()*100+"%)";
    }

    public String getStringLipidPerDay(){
        return Math.round(getLipidPerDay()*100)/100.+" ("+getLipid()*100+"%)";
    }

    @JsonGetter("id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonGetter("weight")
    public double getWeight() {
        return weight;
    }

    @JsonGetter("size")
    public double getSize() {
        return size;
    }

    @JsonGetter("age")
    public int getAge() {
        return age;
    }

    @JsonGetter("sexe")
    public int getSexe() {
        return sexe;
    }

    @JsonGetter("physical_activity")
    public double getPhysical_activity() {
        return physical_activity;
    }

    @JsonGetter("factor")
    public double getFactor() {
        return factor;
    }

    @JsonGetter("carbohydrate")
    public double getCarbohydrate() {
        return carbohydrate;
    }

    @JsonGetter("protein")
    public double getProtein() {
        return protein;
    }

    @JsonGetter("lipid")
    public double getLipid() {
        return lipid;
    }

    @JsonGetter("id_user")
    public int getId_user() {
        return id_user;
    }
}
