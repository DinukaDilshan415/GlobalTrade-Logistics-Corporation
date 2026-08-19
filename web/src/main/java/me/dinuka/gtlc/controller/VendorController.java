package me.dinuka.gtlc.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.dto.vendorDTO;
import me.dinuka.gtlc.service.VendorService;

@Path("/vendor")
public class VendorController {

    @Inject
    private VendorService vendorService;

    @GET
    @Path("/getProfile")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfile(@HeaderParam("Authorization") String authHeader) {
        return vendorService.getVendorProfile(authHeader);
    }

    @POST
    @Path("/saveAccountOpenRequest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAccountOpenRequest(@HeaderParam("Authorization") String authHeader, vendorDTO body) {
        return vendorService.saveVendorAccountOpenRequest(authHeader, body);
    }

}
