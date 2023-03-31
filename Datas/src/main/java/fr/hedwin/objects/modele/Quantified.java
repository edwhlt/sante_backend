package fr.hedwin.objects.modele;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Quantified {
    double getQuantity();
    void setQuantity(double quantity);

    @JsonIgnore
    default String getRoundQuantity(){
        return ""+Math.round(getQuantity()*100)/100.;
    }

}
