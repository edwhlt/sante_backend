package fr.hedwin.objects;

import fr.hedwin.objects.aliment.Nutriment;
import fr.hedwin.objects.elements.Ingredient;
import fr.hedwin.objects.modele.Apport;
import fr.hedwin.objects.modele.Component;
import fr.hedwin.objects.modele.NotAssociateException;
import fr.hedwin.objects.modele.Quantified;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Table {

    public static String getTable(Map<String, ? extends Component> components){
        List<String> strings = new ArrayList<>();
        List<String> modals = new ArrayList<>();
        AtomicBoolean hasFood = new AtomicBoolean(false);
        components.values().forEach(v -> {
            if(v instanceof Quantified) hasFood.set(true);
            modals.add(generateModal(v, v.getElements()));
            try {
                strings.add(getRows(v, true));
            } catch (NotAssociateException e) {
                e.printStackTrace();
            }
        });
        return """
                <table class="table">
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Nom</th>
                        %s
                        <th scope="col">Energie (kcal)</th>
                        <th scope="col">Glucides (g)</th>
                        <th scope="col">Proteines (g)</th>
                        <th scope="col">Lipides (g)</th>
                        <th scope="col">Infos</th>
                    </tr>
                    %s
                </table>
                %s
                """.formatted((hasFood.get() ? "<th scope=\"col\">Quantité</th>\n<th scope=\"col\">Unité</th>" : ""), String.join("", strings), String.join("", modals));

    }

    public static String generateModal(Component component, Map<String, Ingredient> components){
        StringJoiner stringJoiner = new StringJoiner("");
        AtomicBoolean hasFood = new AtomicBoolean(false);
        components.values().forEach(value -> {
            try {
                stringJoiner.add(getRows(value, false));
            } catch (NotAssociateException e) {
                e.printStackTrace();
            }
        });
        return """
                <div class="modal fade" id="modal-%s" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                  <div class="modal-dialog modal-xl">
                    <div class="modal-content">
                      <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Ingrédients</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                      </div>
                      <div class="modal-body">
                        <table class="table">
                           <tr>
                               <th scope="col">Code</th>
                               <th scope="col">Nom</th>
                               <th scope="col">Quantité</th>n<th scope="col">Unité</th>
                               <th scope="col">Energie (kcal)</th>
                                <th scope="col">Glucides (g)</th>
                                <th scope="col">Proteines (g)</th>
                                <th scope="col">Lipides (g)</th>
                            </tr>%s
                        </table>
                      </div>
                      <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                      </div>
                    </div>
                  </div>
                </div>
                """.formatted(component.key(), stringJoiner);
    }

    public static String getRows(Apport component, boolean withBtn) throws NotAssociateException {
        return """
                <tr>
                    <td scope="row">%s</td>
                    <td scope="row">%s</td>
                    %s
                    <td scope="row">%s</td>
                    <td scope="row">%s</td>
                    <td scope="row">%s</td>
                    <td scope="row">%s</td>
                    %s
                </tr>
                """.formatted(
                        component.key(),
                        component.name(),
                        (component instanceof Quantified ? "<td scope=\"row\">"+((Quantified)component).getQuantity()+"</td>" : ""),
                        round(component.getApport(Nutriment.ENERGY)/4.184),
                        round(component.getApport(Nutriment.CARBOHYDRATES)),
                        round(component.getApport(Nutriment.PROTEINS)),
                        round(component.getApport(Nutriment.FAT)),
                        (withBtn ? "<td scope=\"row\"><button type=\"button\" class=\"btn btn-primary btn-sm\" data-bs-toggle=\"modal\" data-bs-target=\"#modal-"+component.key()+"\">Plus...</button></td>" : "")
                );
    }

    public static double round(double d){
        return (double) Math.round(d*100) /100;
    }


}
