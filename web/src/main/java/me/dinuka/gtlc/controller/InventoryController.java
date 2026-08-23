package me.dinuka.gtlc.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.dto.InventoryDTO;
import me.dinuka.gtlc.security.SecurityConstants;
import me.dinuka.gtlc.service.InventoryService;

@Path("/inventory")
public class InventoryController {

    @Inject
    private InventoryService inventoryService;

    @GET
    @RolesAllowed(SecurityConstants.ROLE_ADMIN)
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@HeaderParam("Authorization") String authHeader) {
        return inventoryService.getAllInventory(authHeader);
    }

    @POST
    @RolesAllowed(SecurityConstants.ROLE_ADMIN)
    @Path("/addNewInventory")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewInventory(@HeaderParam("Authorization") String authHeader, InventoryDTO body) {
        return inventoryService.addNewInventory(authHeader, body);
    }
}
