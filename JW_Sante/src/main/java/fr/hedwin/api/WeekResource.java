package fr.hedwin.api;

import fr.hedwin.objects.elements.Week;
import fr.hedwin.sql.DATA;
import fr.hedwin.sql.exceptions.DaoException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("week/{id_profil}")
public class WeekResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id_profil") int id_profil, @QueryParam("nutriments") boolean nutriments) {
        try {
            Map<String, Week> week = nutriments ? DATA.getWeeksWithNutriments(id_profil) : DATA.getWeeks(id_profil);
            return Response.ok(week).build();
        } catch (DaoException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id_week}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id_profil") int id_profil, @PathParam("id_week") String id_week, @QueryParam("nutriments") boolean nutriments) {
        try {
            Week week = nutriments ? DATA.getWeekWithNutriments(id_profil, id_week) : DATA.getWeek(id_profil, id_week);
            return Response.ok(week).build();
        } catch (DaoException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id_week}/day/{day}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDay(@PathParam("id_profil") int id_profil, @PathParam("id_week") String id_week, @PathParam("day") String day, @QueryParam("nutriments") boolean nutriments) {
        try {
            Week week = nutriments ? DATA.getWeekWithNutriments(id_profil, id_week) : DATA.getWeek(id_profil, id_week);
            return Response.ok(week.getElements().get(day)).build();
        } catch (DaoException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@PathParam("id_profil") int id_profil, @QueryParam("week_name") String week_name) {
        try {
            Week week = new Week(null, week_name, id_profil);
            DATA.daoFactory.getWeekDao().add(week);
            return Response.status(Response.Status.CREATED).entity(DATA.getWeek(id_profil, week.key())).build();
        } catch (DaoException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id_week}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id_week") String id_week) throws DaoException {
        DATA.daoFactory.getWeekDao().delete(id_week);
        return Response.ok(id_week).build();
    }
    @PUT
    @Path("/{id_week}/aou_repas_day")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addOrUpdateRepasDay(@PathParam("id_profil") int id_profil, @PathParam("id_week") String id_week, @QueryParam("day") int day,
                                        @QueryParam("last_name") String last_name, @QueryParam("name") String name, @QueryParam("id_repas") int id_repas) {
        try {
            DATA.daoFactory.getWeekDao().addOrUpdateRepas(id_week, day, last_name, name, id_repas);
            return Response.status(Response.Status.CREATED).entity(DATA.getWeek(id_profil, id_week).getElements().get(day+"")).build();
        } catch (DaoException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id_week}/delete_repas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRepas(@PathParam("id_week") String id_week, @QueryParam("day") int day, @QueryParam("repas_name") String repas_name) {
        try {
            DATA.daoFactory.getWeekDao().removeRepas(id_week, day, repas_name);
            return Response.ok("Le repas de "+repas_name+" du jour "+day+" a été supprimé(s) avec succès !").build();
        } catch (DaoException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

}
