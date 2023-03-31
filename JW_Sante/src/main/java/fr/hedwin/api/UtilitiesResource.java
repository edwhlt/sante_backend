package fr.hedwin.api;

import fr.hedwin.sql.exceptions.DaoException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("utils")
public class UtilitiesResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() throws DaoException {
        return Response.ok("Rien à récupérer").build();
    }

}
