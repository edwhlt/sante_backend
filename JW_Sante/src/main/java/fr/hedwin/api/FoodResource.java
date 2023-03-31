package fr.hedwin.api;

import fr.hedwin.objects.elements.FoodElement;
import fr.hedwin.objects.elements.Recette;
import fr.hedwin.sql.DATA;
import fr.hedwin.sql.exceptions.DaoException;
import fr.hedwin.sql.utils.Selectable;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/food")
public class FoodResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context UriInfo uriInfo) throws DaoException {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        List<Object[]> objectList = new ArrayList<>();
        queryParams.forEach((k, l) -> {
            objectList.add(new Object[]{k, l.get(0)});
        });
        Object[][] objects = objectList.toArray(Object[][]::new);
        Map<String, FoodElement> foodElementMap = DATA.daoFactory.getFoodElementDao().select(new Selectable(false, objects));
        return Response.ok(foodElementMap).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(FoodElement foodElement) throws DaoException {
        DATA.daoFactory.getFoodElementDao().add(foodElement);
        return Response.status(Response.Status.CREATED).entity(foodElement).build();
    }

}
