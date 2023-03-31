package fr.hedwin.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.hedwin.RandomString;
import fr.hedwin.objects.User;
import fr.hedwin.sql.DATA;
import fr.hedwin.sql.exceptions.DaoException;
import fr.hedwin.sql.utils.Selectable;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Path("/user")
public class UserResource {

    private static final Map<String, User> users_connected = new HashMap<>();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProfiles(@PathParam("id") int id) throws DaoException {
        Map<Integer, User> users = DATA.daoFactory.getUserDao().select(new Selectable(new Object[]{"id_user", id}));
        return Response.ok(users.get(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProfiles(@QueryParam("email") String email, @QueryParam("password") String password) throws DaoException {
        Map<Integer, User> users = DATA.daoFactory.getUserDao().select(new Selectable(new Object[]{"email", email}, new Object[]{"password", password}));
        return Response.ok(users).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProfiles(ObjectNode objectNode) throws DaoException {
        String email = objectNode.get("email").asText();
        String password = objectNode.get("password").asText();
        User user = new User(0, objectNode.get("name").asText(), email, password, new Date());
        DATA.daoFactory.getUserDao().add(user);
        Map<Integer, User> users = DATA.daoFactory.getUserDao().select(new Selectable(new Object[]{"email", email}, new Object[]{"password", password}));
        return Response.ok(users).build();
    }

    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response alreadyConnected(@QueryParam("login_key") String login_key){
        System.out.println(users_connected.values());
        User user = users_connected.get(login_key);
        return Response.ok(user).build();
    }

    @PUT
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response conected(@QueryParam("email") String email, @QueryParam("password") String password) throws DaoException {
        Map<Integer, User> users = DATA.daoFactory.getUserDao().select(new Selectable(new Object[]{"email", email}, new Object[]{"password", password}));

        if(!users.isEmpty()){
            User user = users.values().iterator().next();
            String login_key = new RandomString(50).nextString();
            users_connected.put(login_key, user);

            System.out.println(users_connected.values());

            Map<String, Object> value = new HashMap<>(){{
                put("user", user); put("login_key", login_key);
            }};

            return Response.ok(value).build();
        }else{
            return Response.noContent().build();
        }
    }


}
