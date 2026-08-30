package me.dinuka.gtlc.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.security.SecurityConstants;
import me.dinuka.gtlc.service.MonitoringService;

@Path("/monitoring")
public class MonitoringController {

    @Inject
    private MonitoringService monitoringService;

    @GET
    @RolesAllowed(SecurityConstants.ROLE_ADMIN)
    @Path("/getSystemMetrics")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystemMetrics(@HeaderParam("Authorization") String authHeader) {
        return monitoringService.getSystemMetrics(authHeader);
    }

}
