package fr.hedwin.api;

import fr.hedwin.objects.elements.Ingredient;
import fr.hedwin.objects.elements.Recette;
import fr.hedwin.objects.elements.Repas;
import fr.hedwin.objects.manage.RepasMap;
import fr.hedwin.sql.DATA;
import fr.hedwin.sql.exceptions.DaoException;
import fr.hedwin.sql.utils.Selectable;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/repas/{id_profil}")
public class RepasResource {

    @GET
    @Path("/{id_repas}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id_profil") int id_profil, @PathParam("id_repas") String id, @QueryParam("nutriments") boolean nutriments) throws DaoException {
        Repas repas = nutriments ? DATA.getRepasWithNutriment(id_profil).get(id) : DATA.getRepas(id_profil).get(id);
        return Response.ok(repas).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@PathParam("id_profil") int id_profil, @QueryParam("nutriments") boolean nutriments) throws DaoException {
        RepasMap repasMap = nutriments ? DATA.getRepasWithNutriment(id_profil) : DATA.getRepas(id_profil);
        return Response.ok(repasMap).build();
    }

    @GET
    @Path("/filter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilter(@PathParam("id_profil") int id_profil, @Context UriInfo uriInfo) throws DaoException {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        List<Object[]> objectList = new ArrayList<>();
        queryParams.forEach((k, l) -> {
            objectList.add(new Object[]{"repas."+k, l.get(0)});
        });
        objectList.add(new Object[]{"repas.id_profil", id_profil});
        Object[][] objects = objectList.toArray(Object[][]::new);
        Map<String, Repas> repasMap = DATA.daoFactory.getRepasDao().select(new Selectable(false, objects));
        return Response.ok(repasMap).build();
    }

    @POST
    //@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Repas repas) throws DaoException {
        DATA.addRepas(repas);
        return Response.status(Response.Status.CREATED).entity(DATA.getRepas(repas.getIdProfil()).get(repas.key())).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Repas repas) throws DaoException {
        DATA.daoFactory.getRepasDao().update(repas);
        return Response.status(Response.Status.CREATED).entity(repas).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@QueryParam("id_repas") String id_repas)  {
        try {
            DATA.removeRepas(id_repas);
            return Response.ok(id_repas).build();
        } catch (DaoException e) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity("Impossible de supprimer: "+e.getMessage()).type(MediaType.TEXT_PLAIN).build());
        }
    }

    @POST
    @Path("/{id_repas}/add_ing")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createIng(@PathParam("id_repas") String id_repas, @QueryParam("id") String id, @QueryParam("quantity") double quantity) throws DaoException {
        DATA.daoFactory.getRepasDao().addIngredient(id_repas, new Ingredient<>(quantity){
            @Override
            public String key() {
                return id;
            }
        });
        return Response.ok("Ingrédient ("+id+") ajouté au repas ("+id_repas+") avec succès !").build();
    }

    @DELETE
    @Path("/{id_repas}/del_ing")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteIng(@PathParam("id_repas") String id_repas, @QueryParam("ids") String ids) throws DaoException {
        DATA.daoFactory.getRepasDao().removeIngredient(id_repas, ids.split(","));
        return Response.ok("Ingrédient(s) ("+ids+") supprimé(s) du repas ("+id_repas+") avec succès !").build();
    }

    @POST
    @Path("/{id_repas}/add_rec")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRecette(@PathParam("id_repas") String id_repas, @QueryParam("id") String id, @QueryParam("guest") double quantity) throws DaoException {
        DATA.daoFactory.getRepasDao().addRecette(id_repas, new Ingredient<>(quantity){
            @Override
            public String key() {
                return id;
            }
        });
        return Response.ok("Recette ("+id+") ajouté au repas ("+id_repas+") avec succès !").build();
    }

    @DELETE
    @Path("/{id_repas}/del_rec")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRecette(@PathParam("id_repas") String id_repas, @QueryParam("ids") String ids) throws DaoException {
        DATA.daoFactory.getRepasDao().removeRecette(id_repas, ids.split(","));
        return Response.ok("Recette(s) ("+ids+") supprimé(s) du repas ("+id_repas+") avec succès !").build();
    }

}
