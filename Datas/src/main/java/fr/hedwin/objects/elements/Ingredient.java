package fr.hedwin.objects.elements;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.hedwin.objects.aliment.Nutriment;
import fr.hedwin.objects.modele.*;

import java.util.function.Function;

public class Ingredient<T extends Apport> extends Element<T> implements Quantified, Cloneable  {

    @JsonProperty("quantity")
    private double quantity;

    public Ingredient(double quantity) {
        this(quantity, null);
    }

    public Ingredient(){
        super(null);
    }

    public Ingredient(double quantity, T apport) {
        super(apport);
        this.quantity = quantity;
    }

    @JsonGetter("quantity")
    @Override
    public double getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public double getApport(Nutriment nutriment) throws NotAssociateException {
        if(getElement() == null) throw new NotAssociateException(this);
        return getElement().getApport(nutriment)*proportion();
    }

    public double proportion(){
        Function<Double, Double> q = (def) -> getElement() instanceof Quantified ? ((Quantified) getElement()).getQuantity() : def;
        Unitable.Unit unit = getElement() instanceof Unitable ? ((Unitable) getElement()).getUnit() : Unitable.Unit.GUEST;

        return switch (unit) {
            case GRAMME, MILILITRE -> quantity / q.apply(100.);
            case CUILLERE_A_SOUPE -> quantity * 15 / q.apply(100.);
            case CUILLERE_A_CAFE -> quantity * 5 / q.apply(100.);
            default -> quantity / q.apply(1.);
        };
    }

    @Override
    protected Ingredient<T> clone() throws CloneNotSupportedException {
        return (Ingredient<T>) super.clone();
    }
}
