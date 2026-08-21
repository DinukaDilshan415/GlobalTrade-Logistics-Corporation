package me.dinuka.gtlc.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.service.GetService;

@Path("/get")
public class GetController {

    @Inject
    private GetService getService;

    @GET
    @Path("/countries")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCountries(){
        return getService.getCountries();
    }

    @GET
    @Path("/countriesWithWarehouses")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCountriesWithWarehouses(){
        return getService.getCountriesWithWarehouses();
    }
}
