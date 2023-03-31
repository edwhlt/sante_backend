package fr.hedwin.objects.modele;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.hedwin.objects.aliment.Nutriment;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Element<T extends Apport> implements Apport {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("element")
    private T e;

    public Element(T e){
        this.e = e;
    }

    @JsonGetter("element")
    public T getElement() {
        return e;
    }

    public void setElement(T e) {
        this.e = e;
    }

    @Override
    public String key() {
        return e.key();
    }

    @Override
    public String name() {
        return e.name();
    }

}
