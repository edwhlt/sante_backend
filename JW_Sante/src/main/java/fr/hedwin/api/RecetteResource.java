package fr.hedwin.api;

import fr.hedwin.objects.elements.FoodElement;
import fr.hedwin.objects.elements.Ingredient;
import fr.hedwin.objects.elements.Recette;
import fr.hedwin.objects.manage.RecetteMap;
import fr.hedwin.sql.DATA;
import fr.hedwin.sql.exceptions.DaoException;
import fr.hedwin.sql.utils.Selectable;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/recette/{id_profil}")
public class RecetteResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@PathParam("id_profil") int id_profil, @QueryParam("nutriments") boolean nutriments) throws DaoException {
        RecetteMap recetteMap = nutriments ? DATA.getRecetteWithNutriment(id_profil) : DATA.getRecette(id_profil);
        return Response.ok(recetteMap).build();
    }

    @GET
    @Path("/filter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilter(@PathParam("id_profil") int id_profil, @Context UriInfo uriInfo) throws DaoException {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        List<Object[]> objectList = new ArrayList<>();
        queryParams.forEach((k, l) -> {
            objectList.add(new Object[]{"recette."+k, l.get(0)});
        });
        objectList.add(new Object[]{"recette.id_profil", id_profil});
        Object[][] objects = objectList.toArray(Object[][]::new);
        Map<String, Recette> recetteMap = DATA.daoFactory.getRecetteDao().select(new Selectable(false, objects));
        return Response.ok(recetteMap).build();
    }

    @GET
    @Path("/{id_recette}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id_profil") int id_profil, @PathParam("id_recette") String id, @QueryParam("nutriments") boolean nutriments) {
        try {
            Recette recette = nutriments ? DATA.getRecetteWithNutriment(id_profil).get(id) : DATA.getRecette(id_profil).get(id);
            return Response.ok(recette).build();
        } catch (DaoException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Recette recette) throws DaoException {
        DATA.addRecette(recette);
        return Response.status(Response.Status.CREATED).entity(DATA.getRecette(recette.getIdProfil()).get(recette.key())).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Recette recette) throws DaoException {
        DATA.daoFactory.getRecetteDao().update(recette);
        return Response.status(Response.Status.CREATED).entity(recette).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@QueryParam("id_recette") String id_recette) throws DaoException {
        DATA.removeRecette(id_recette);
        return Response.ok(id_recette).build();
    }

    @POST
    @Path("/{id_recette}/add_ing")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createIng(@PathParam("id_recette") String id_recette, @QueryParam("id") String id, @QueryParam("quantity") double quantity) throws DaoException {
        DATA.daoFactory.getRecetteDao().addIngredient(id_recette, new Ingredient<>(quantity){
            @Override
            public String key() {
                return id;
            }
        });
        return Response.ok("Ingrédient ("+id+") ajouté à la recette ("+id_recette+") avec succès !").build();
    }

    @DELETE
    @Path("/{id_recette}/del_ing")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteIng(@PathParam("id_recette") String id_recette, @QueryParam("ids") String ids) throws DaoException {
        DATA.daoFactory.getRecetteDao().removeIngredient(id_recette, ids.split(","));
        return Response.ok("Ingrédient(s) ("+ids+") supprimé(s) de la recette ("+id_recette+") avec succès !").build();
    }

}
