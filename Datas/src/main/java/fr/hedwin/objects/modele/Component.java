package fr.hedwin.objects.modele;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.hedwin.objects.aliment.Nutriment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class Component<T extends Apport> implements Apport{

    @Override
    public double getApport(Nutriment nutriment) throws RuntimeException{
        return elements.values().stream().mapToDouble(e -> {
            try {
                return e.getApport(nutriment);
            } catch (NotAssociateException notAssociateException) {
                throw new RuntimeException(notAssociateException);
            }
        }).sum();
    }

    @JsonGetter("nutriments")
    public Map<Nutriment, Double> getNutriments() throws RuntimeException {
        return Arrays.stream(Nutriment.values()).collect(Collectors.toMap(n -> n, this::getApport));
    }

    public final void updateComponent(BiConsumer<String, T> consumer){
        getElements().forEach(consumer);
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final Map<String, T> elements = new HashMap<>();

    @JsonGetter("elements")
    public Map<String, T> getElements() {
        return elements;
    }

    public T getElement(Predicate<? super T> predicate){
        return getElements().values().stream().filter(predicate).findAny().orElse(null);
    }

    public void addElement(String key, T e){
        elements.putIfAbsent(key, e);
    }

    public void updateElement(String key, T e){
        elements.replace(key, e);
    }

}
