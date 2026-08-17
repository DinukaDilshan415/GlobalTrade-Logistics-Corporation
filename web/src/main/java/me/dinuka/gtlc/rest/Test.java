package me.dinuka.gtlc.rest;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.security.SecurityConstants;

import java.util.Map;

@Path( "/test")
@DeclareRoles({SecurityConstants.ROLE_CUSTOMER, SecurityConstants.ROLE_ADMIN, SecurityConstants.CUSTOMS_AGENT, SecurityConstants.MANAGER})
@RolesAllowed(SecurityConstants.ROLE_CUSTOMER)
public class Test {

    @GET
    public String test() {
        System.out.println( "Hello World!");
        return "Hello World!";
    }

    @POST
    @Consumes("application/json")
    public Response post(String json) {
        System.out.println(json);
        return Response.ok().entity(Map.of("message", "Hello World!")).build();
    }
}
