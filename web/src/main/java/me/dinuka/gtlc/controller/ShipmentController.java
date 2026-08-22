package me.dinuka.gtlc.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.security.SecurityConstants;
import me.dinuka.gtlc.service.ShipmentService;

import java.util.Map;

@Path("/shipment")
public class ShipmentController {

    @Inject
    private ShipmentService shipmentService;

    @GET
    @RolesAllowed(SecurityConstants.ROLE_ADMIN)
    @Path("/getAllPendingShipmets")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPendingShipmets(@HeaderParam("Authorization") String authHeader) {
        return shipmentService.getAllPendingShipmets(authHeader);
    }

    @GET
    @RolesAllowed(SecurityConstants.ROLE_ADMIN)
    @Path("/getAllActiveShipments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllActiveShipments(@HeaderParam("Authorization") String authHeader) {
        return shipmentService.getAllActiveShipments(authHeader);
    }

    @PUT
    @RolesAllowed(SecurityConstants.ROLE_ADMIN)
    @Path("/updatePendingShipment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePendingShipment(@HeaderParam("Authorization") String authHeader, Map<String, String> body) {
        return shipmentService.updatePendingShipment(authHeader, body);
    }
}
