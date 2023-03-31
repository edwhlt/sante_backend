package fr.hedwin.objects.manage;

import fr.hedwin.objects.elements.FoodElement;
import fr.hedwin.objects.elements.Ingredient;
import fr.hedwin.objects.elements.Recette;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class RecetteMap extends HashMap<String, Recette> {

    public RecetteMap(Map<String, Recette> map) {
        putAll(map);
    }

    public void updateElements(BiConsumer<String, Ingredient<FoodElement>> consumer){
        this.forEach((k, v) -> v.updateElements(consumer));
    }

}
