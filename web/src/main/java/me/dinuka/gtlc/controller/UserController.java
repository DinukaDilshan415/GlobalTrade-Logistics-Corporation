package me.dinuka.gtlc.controller;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.dto.UserDTO;
import me.dinuka.gtlc.remote.UserRemoteService;
import me.dinuka.gtlc.service.UserService;

@Path("/user")
public class UserController {

    @Inject
    private UserService userService;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(UserDTO body){
        return userService.registerUser(body);
    }

    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@HeaderParam("Authorization") String authHeader){
        return userService.getAll(authHeader);
    }
}
