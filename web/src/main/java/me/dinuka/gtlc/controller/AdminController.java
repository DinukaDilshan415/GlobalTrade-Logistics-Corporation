package me.dinuka.gtlc.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.security.SecurityConstants;
import me.dinuka.gtlc.service.AdminService;

import java.util.Map;

@Path("/admin")
public class AdminController {

    @Inject
    private AdminService adminService;

    @POST
    @Path("/saveNewUser")
    @RolesAllowed(SecurityConstants.ROLE_ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveNewUser(@HeaderParam("Authorization") String authHeader, Map<String, Object> body){
        return adminService.saveNewUser(authHeader, body);
    }

    @GET
    @Path("/getAllUsers")
    @RolesAllowed(SecurityConstants.ROLE_ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers(@HeaderParam("Authorization") String authHeader){
        return adminService.getAllUsers(authHeader);
    }

}
