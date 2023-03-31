package fr.hedwin.api;

import fr.hedwin.Main;
import fr.hedwin.objects.Profil;
import fr.hedwin.objects.elements.FoodElement;
import fr.hedwin.objects.elements.Ingredient;
import fr.hedwin.objects.modele.NotAssociateException;
import fr.hedwin.sql.DATA;
import fr.hedwin.sql.exceptions.DaoException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.util.*;

@Path("/resource")
public class FileResource {

    @GET
    @Path("/excel/{id_profil}/{id_week}")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public StreamingOutput exportReadingsAsExcel(@PathParam("id_profil") int id, @PathParam("id_week") String id_week, @Context HttpServletResponse webResponse) throws DaoException, IOException, NotAssociateException {
        Profil profil = DATA.getProfil(id);
        Workbook workbook = Main.createPlanningExcel(profil, DATA.getWeekWithNutriments(id, id_week).getElements());
        webResponse.setHeader("Content-Disposition", "attachment; filename=\"planning_"+id_week+"_"+profil.getName()+".xlsx\"");

        return output -> {
            try {
                workbook.write(output);
            } catch (Exception e) {
                throw new WebApplicationException(e);
            }
        };
    }

    @GET
    @Path("/weeklyList/{id_profil}/{id_week}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getWeeklyList(@PathParam("id_profil") int id, @PathParam("id_week") String id_week, @Context HttpServletResponse webResponse) throws DaoException {
        Map<String, Ingredient<FoodElement>> is = Main.saveList(DATA.getWeekWithNutriments(id, id_week).getElements());

        StringJoiner sj = new StringJoiner("\n");
        sj.add(String.join(";", Arrays.asList("Code barre", "Nom", "Quantités")));
        is.forEach((k, v) -> {
            FoodElement foodElement = v.getElement();
            String unity = foodElement.weightUnity() > 0 ? " (Unité: "+Math.round(v.getQuantity()/foodElement.weightUnity()*100)/100.+")" : "";
            //sj.add(new Formatter().format("%-20s%-50s%-10s", k, v.name(), Math.round(v.getQuantity()*100)/100.+""+foodElement.getUnit().getUnit()) +unity);
            sj.add(String.join(";", Arrays.asList(k, v.name(), Math.round(v.getQuantity()*100)/100.+foodElement.getUnit().getUnit())+unity));
        });
        webResponse.setHeader("Content-Disposition", "attachment; filename=\"list_ingredients_"+id_week+".csv\"");

        return Response.ok(sj.toString()).build();
    }





}
