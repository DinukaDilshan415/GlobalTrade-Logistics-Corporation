package me.dinuka.gtlc.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path( "/test")
public class Test {

    @GET
    public String test() {
        return "Hello World!";
    }
}
