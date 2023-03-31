package fr.hedwin.api;

import fr.hedwin.object.json.exceptions.NotFoundElement;
import fr.hedwin.objects.aliment.Aliment;
import fr.hedwin.sql.DATA;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/product/{code}")
public class ProductResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Aliment get(@PathParam("code") String code) throws NotFoundElement {
        return DATA.getAliment(code);
    }


}
