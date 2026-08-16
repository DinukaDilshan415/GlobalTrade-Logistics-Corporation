package me.dinuka.gtlc.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path( "/test")
public class Test {

    @GET
    public String test() {
        return "Hello World!";
    }

    @POST
    @Consumes("application/json")
    public Response post(String json) {
        System.out.println(json);
        return Response.ok().entity(Map.of("message", "Hello World!")).build();
    }
}
