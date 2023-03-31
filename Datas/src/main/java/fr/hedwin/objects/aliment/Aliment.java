package fr.hedwin.objects.aliment;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.hedwin.objects.modele.Apport;

import java.io.IOException;
import java.util.Map;

@JsonSerialize(using = Aliment.AlimentSerializer.class)
public class Aliment implements Apport {

    public static class AlimentSerializer extends JsonSerializer<Aliment> {
        @Override
        public void serialize(Aliment aliment, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("name", aliment.name);
            jsonGenerator.writeStringField("code", aliment.code);
            jsonGenerator.writeObjectField("nutriments", aliment.nutriments);
            jsonGenerator.writeEndObject();
        }
    }

    @JsonProperty("name")
    private String name;
    @JsonProperty("code")
    private String code;
    @JsonProperty("nutriments")
    private Map<Nutriment, Nutriment.Infos> nutriments;

    public Aliment(String name, String code, Map<Nutriment, Nutriment.Infos> nutriments) {
        this.name = name;
        this.code = code;
        this.nutriments = nutriments;
    }


    public Aliment(){}

    @JsonGetter("nutriments")
    public Map<Nutriment, Nutriment.Infos> getNutriments() {
        return nutriments;
    }

    public Nutriment.Infos getNutriment(Nutriment nutriment){
        return nutriments.getOrDefault(nutriment, new Nutriment.Infos(.0, ""));
    }

    @Override
    public double getApport(Nutriment nutriment) {
        return nutriments.get(nutriment) != null ? nutriments.get(nutriment).getValue_100g() : 0;
    }

    @JsonGetter("code")
    @Override
    public String key() {
        return code;
    }

    @JsonGetter("name")
    @Override
    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
