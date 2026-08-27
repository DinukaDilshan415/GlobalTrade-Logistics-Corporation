package me.dinuka.gtlc.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.security.SecurityConstants;
import me.dinuka.gtlc.service.AlertAndNotifyService;

import java.util.Map;

@Path( "/alert")
public class AlertController {

    @Inject
    private AlertAndNotifyService alertAndNotifyService;

    @GET
    @Path("/getAll")
    @RolesAllowed(SecurityConstants.ROLE_ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@HeaderParam("Authorization") String authHeader){
        return alertAndNotifyService.getAll(authHeader);
    }

    @PUT
    @RolesAllowed({SecurityConstants.ROLE_ADMIN})
    @Path("/updateStatus")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStatus(@HeaderParam("Authorization") String authHeader, Map<String, String> body) {
        return alertAndNotifyService.updateStatus(authHeader, body);
    }
}
