package fr.hedwin.objects.elements;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.hedwin.objects.modele.Component;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Week extends Component<Day> {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("id_profil")
    private final int id_profil;

    public Week(String id, String name, int id_profil){
        this.id = id;
        this.name = name;
        this.id_profil = id_profil;
    }

    @JsonGetter("id")
    @Override
    public String key() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonGetter("name")
    @Override
    public String name() {
        return name;
    }

    @JsonGetter("id_profil")
    public int getId_profil() {
        return id_profil;
    }
}
