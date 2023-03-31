package fr.hedwin.objects.manage;

import fr.hedwin.objects.elements.FoodElement;
import fr.hedwin.objects.elements.Ingredient;
import fr.hedwin.objects.elements.Recette;
import fr.hedwin.objects.elements.Repas;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class RepasMap extends HashMap<String, Repas> {

    public RepasMap(Map<String, Repas> map) {
        putAll(map);
    }

    public void updateElements(BiConsumer<String, Ingredient<FoodElement>> consumer){
        this.forEach((k, v) -> v.updateElements(consumer));
    }

    public void updateRecettes(BiConsumer<String, Ingredient<Recette>> consumer){
        this.forEach((k, v) -> v.updateRecettes(consumer));
    }

}
