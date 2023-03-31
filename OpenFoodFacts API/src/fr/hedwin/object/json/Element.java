package fr.hedwin.object.json;

import java.util.Map;

public class Element {

    private String code;
    private int status;
    private Product product;
    private String status_verbose;

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }

    public String getStatus_verbose() {
        return status_verbose;
    }

    public String getName(){
        return product.ingredients_tags != null ? product.ingredients_tags.stream().filter(s -> s.startsWith("en:"))
                .map(s -> s.replace("en:", "")).findFirst().orElse("Non définie") : "Non définie";
    }

    public Map<String, Object> getNutriments(){
        return product.nutriments;
    }


}
