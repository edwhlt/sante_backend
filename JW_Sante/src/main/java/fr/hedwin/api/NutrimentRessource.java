package fr.hedwin.api;

import fr.hedwin.objects.elements.FoodElement;
import fr.hedwin.objects.elements.Recette;
import fr.hedwin.objects.elements.Repas;
import fr.hedwin.objects.elements.Week;
import fr.hedwin.sql.DATA;
import fr.hedwin.sql.exceptions.DaoException;
import fr.hedwin.sql.utils.Selectable;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;

@Path("/nutriments/{id_profil}")
public class NutrimentRessource {

    @GET
    @Path("/repas/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRepasNutriments(@PathParam("id_profil") int id_profil, @PathParam("id") String id) throws DaoException {
        Repas repas = DATA.getRepasWithNutriment(id_profil).get(id);
        return Response.ok(repas.getNutriments()).build();
    }

    @GET
    @Path("/recette/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecetteNutriments(@PathParam("id_profil") int id_profil, @PathParam("id") String id) throws DaoException {
        Recette recette = DATA.getRecetteWithNutriment(id_profil).get(id);
        return Response.ok(recette.getNutriments()).build();
    }

    @GET
    @Path("/week/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWeekNutriments(@PathParam("id_profil") int id_profil, @PathParam("id") String id) throws DaoException {
        Week week = DATA.getWeekWithNutriments(id_profil, id);
        return Response.ok(week.getNutriments()).build();
    }

}
