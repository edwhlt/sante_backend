package fr.hedwin.objects.elements;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.hedwin.objects.aliment.Aliment;
import fr.hedwin.objects.aliment.Nutriment;
import fr.hedwin.objects.modele.Element;
import fr.hedwin.objects.modele.NotAssociateException;
import fr.hedwin.objects.modele.Unitable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public class FoodElement extends Element<Aliment> implements Unitable {

    @JsonProperty("id")
    private String id;
    @JsonProperty("code")
    private String code;
    @JsonProperty("name")
    private String name;
    @JsonProperty("unit")
    private Unit unit;

    @JsonProperty("weight_unity")
    private double weightUnity;

    public FoodElement(String id, String code, String name, String unit){
        super(null);
        this.id = id;
        this.code = code;
        this.name = name;
        this.unit = Unit.getUnit(unit, this);
    }

    public FoodElement(){
        super(null);
    }

    @JsonGetter("id")
    @Override
    public String key() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonGetter("code")
    public String getCode() {
        return code;
    }

    @JsonGetter("name")
    @Override
    public String name() {
        return name;
    }

    @JsonGetter("unit")
    @Override
    public Unit getUnit() {
        return unit;
    }

    @Override
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @JsonGetter("weight_unity")
    @Override
    public double weightUnity() {
        return weightUnity;
    }

    @Override
    public void setWeightUnity(double weightUnity) {
        this.weightUnity = weightUnity;
    }

    @Override
    public double getApport(Nutriment nutriment) throws NotAssociateException {
        if(getElement() == null) throw new NotAssociateException(this);
        return getElement().getApport(nutriment);
    }

}
