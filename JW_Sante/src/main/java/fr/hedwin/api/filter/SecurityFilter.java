package fr.hedwin.api.filter;

import fr.hedwin.objects.ApiUser;
import fr.hedwin.sql.DATA;
import fr.hedwin.sql.exceptions.DaoException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
import java.util.Map;

@Provider
public class SecurityFilter  implements ContainerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void filter(ContainerRequestContext requestContext){
        String authorization = requestContext.getHeaderString("authorization");

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        System.out.println("----------------------------- Authentification -----------------------------");
        System.out.println(requestContext.getHeaders().toString());
        if(authorization != null) System.out.println("Api Key: "+String.join(", ", authorization));
        System.out.println("----------------------------------------------------------------------------");

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }
        // Récupération de la clé Bearer
        String token = authHeader.substring(BEARER_PREFIX.length());


        try {
            ApiUser api_user = DATA.getApiAccess().values().stream().filter(au -> au.getApiKey().equals(token)).findAny().orElse(null);
            if(api_user == null) throw new Exception();
        } catch (Exception e) {
            Response response = Response.status(Response.Status.UNAUTHORIZED).entity("Vous n'avez pas la permission de réaliser des requêtes, contacter l'administrateur !").build();
            requestContext.abortWith(response);
        }

        // Stocker la clé Bearer pour utilisation ultérieure par d'autres filtres ou ressources
        requestContext.setProperty("token", token);


    }
}