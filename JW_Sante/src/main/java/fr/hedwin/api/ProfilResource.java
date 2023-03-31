package fr.hedwin.api;

import fr.hedwin.objects.Profil;
import fr.hedwin.objects.elements.Recette;
import fr.hedwin.sql.DATA;
import fr.hedwin.sql.exceptions.DaoException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/profil")
public class ProfilResource {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProfiles(@PathParam("id") int id) throws DaoException {
        return Response.ok(DATA.getProfil(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("id_user") int id) throws DaoException {
        Map<Integer, Profil> profils = DATA.getUserProfils(id);
        return Response.ok(profils).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Profil profil) throws DaoException {
        DATA.daoFactory.getProfilDao().add(profil);
        return Response.status(Response.Status.CREATED).entity(DATA.getProfil(profil.getId())).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Profil profil) throws DaoException {
        DATA.daoFactory.getProfilDao().update(profil);
        return Response.status(Response.Status.ACCEPTED).entity(profil).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) throws DaoException {
        DATA.deleteProfil(id);
        return Response.ok("Profil ("+id+") supprimé avec succès !").build();
    }
}

