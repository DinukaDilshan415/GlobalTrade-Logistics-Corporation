package me.dinuka.gtlc.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.dto.vendorDTO;
import me.dinuka.gtlc.security.SecurityConstants;
import me.dinuka.gtlc.service.VendorService;

import java.util.Map;

@Path("/vendor")
public class VendorController {

    @Inject
    private VendorService vendorService;

    @GET
    @RolesAllowed(SecurityConstants.ROLE_CUSTOMER)
    @Path("/getProfile")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfile(@HeaderParam("Authorization") String authHeader) {
        return vendorService.getVendorProfile(authHeader);
    }

    @GET
    @RolesAllowed(SecurityConstants.ROLE_ADMIN)
    @Path("/getAllVendors")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllVendors(@HeaderParam("Authorization") String authHeader) {
        return vendorService.getAllVendors(authHeader);
    }

    @POST
    @RolesAllowed(SecurityConstants.ROLE_CUSTOMER)
    @Path("/saveAccountOpenRequest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAccountOpenRequest(@HeaderParam("Authorization") String authHeader, vendorDTO body) {
        return vendorService.saveVendorAccountOpenRequest(authHeader, body);
    }

    @PUT
    @RolesAllowed(SecurityConstants.ROLE_ADMIN)
    @Path("/updateStatus")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStatus(@HeaderParam("Authorization") String authHeader, Map<String, String> body) {
        return vendorService.updateStatus(authHeader, body);
    }

}
