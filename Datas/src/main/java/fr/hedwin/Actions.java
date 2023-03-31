package fr.hedwin;

import fr.hedwin.object.json.Element;
import fr.hedwin.object.json.exceptions.NotFoundElement;
import fr.hedwin.object.json.exceptions.NotFoundElementOFFAPI;
import fr.hedwin.objects.CiqualIngredient;
import fr.hedwin.objects.aliment.Aliment;
import fr.hedwin.objects.aliment.Nutriment;

import java.util.HashMap;
import java.util.Map;

public class Actions {

    private static Aliment generateAliment(Element element){
        Map<Nutriment, Nutriment.Infos> nutriments = new HashMap<>();
        for(Map.Entry<String, Object> entry : element.getNutriments().entrySet()){
            Nutriment nutriment = Nutriment.getNutriment(entry.getKey());
            if(nutriment == null) continue;
            if(!nutriments.containsKey(nutriment)){
                Nutriment.Infos infos = new Nutriment.Infos(.0, "");
                nutriments.put(nutriment, infos);
                Nutriment.updateNutrimentInfos(nutriment, entry.getKey(), entry.getValue(), infos);
            }else Nutriment.updateNutrimentInfos(nutriment, entry.getKey(), entry.getValue(), nutriments.get(nutriment));
        }
        return new Aliment(element.getName(), element.getCode(), nutriments);
    }

    public static Aliment searchAliment(String code) throws NotFoundElement {
        try {
            return generateAliment(OFFAPI.getProduct(code));
        } catch (NotFoundElementOFFAPI notFoundElementOFFAPI) {
            throw new NotFoundElement(notFoundElementOFFAPI.getMessage());
        }
    }

    public static Aliment generateAlimentFromCiqual(CiqualIngredient element){
        Map<Nutriment, Nutriment.Infos> nutriments = new HashMap<>();
        for(Map.Entry<String, CiqualIngredient.Nutriment> entry : element.getNutrimentMap().entrySet()){
            Nutriment nutriment = Nutriment.getCiqualNutriment(entry.getKey());
            double v = entry.getValue().getQuantity();
            if(nutriment == null) continue;
            if(!nutriments.containsKey(nutriment)){
                Nutriment.Infos infos = new Nutriment.Infos(v, entry.getValue().getUnit());
                nutriments.put(nutriment, infos);
            }
        }
        return new Aliment(element.getName(), element.getCode(), nutriments);
    }

}
