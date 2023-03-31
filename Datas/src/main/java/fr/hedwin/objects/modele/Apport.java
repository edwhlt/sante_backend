package fr.hedwin.objects.modele;

import fr.hedwin.objects.aliment.Nutriment;

public interface Apport extends Named {

    double getApport(Nutriment nutriment) throws NotAssociateException;

    default String getStringApport(Nutriment nutriment) throws NotAssociateException{
        double d = Math.round(getProportionApport(nutriment));
        return Math.round(getApport(nutriment)*100)/100.+
                (!Double.isNaN(d) ? " ("+Math.round(getProportionApport(nutriment)*100)+"%)" : "");
    }

    default double getProportionApport(Nutriment nutriment) throws NotAssociateException{
        if(nutriment.equals(Nutriment.PROTEINS) || nutriment.equals(Nutriment.CARBOHYDRATES)){
            return getApport(nutriment)*4 / (getApport(Nutriment.ENERGY)/4.184);
        }else if(nutriment.equals(Nutriment.FAT)){
            return getApport(nutriment)*9 / (getApport(Nutriment.ENERGY)/4.184);
        }else{
            return Double.NaN;
        }
    }

}
