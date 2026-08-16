package me.dinuka.gtlc.controller;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
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
}
