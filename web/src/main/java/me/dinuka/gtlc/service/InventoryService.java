package me.dinuka.gtlc.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.dto.InventoryDTO;
import me.dinuka.gtlc.ejb.InventorySessionBean;
import me.dinuka.gtlc.util.JwtUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequestScoped
public class InventoryService {

    @Inject
    private InventorySessionBean inventorySessionBean;

    public Response getAllInventory(String authHeader){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String username = jwt.getSubject();

                String allInventory = inventorySessionBean.getAllInventory();

                return Response.status(Response.Status.OK)
                        .entity(allInventory)
                        .build();
            } else {
                System.out.println("Invalid or expired token:");
                return Response.status(Response.Status.UNAUTHORIZED).entity(
                        Map.of("message", "Invalid or expired token")
                ).build();
            }
        } else {
            System.out.println("Authorization header is missing:");
            return Response.status(Response.Status.UNAUTHORIZED).entity(
                    Map.of("message", "Authorization header is missing")
            ).build();
        }
    }

    public Response addNewInventory(String authHeader, InventoryDTO body){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String username = jwt.getSubject();

                inventorySessionBean.addNewInventory(body);

                return Response.status(Response.Status.OK)
                        .entity(Map.of(
                                "status", true,
                                "message", "Inventory addition in progress"
                        ))
                        .build();
            } else {
                System.out.println("Invalid or expired token:");
                return Response.status(Response.Status.UNAUTHORIZED).entity(
                        Map.of("message", "Invalid or expired token")
                ).build();
            }
        } else {
            System.out.println("Authorization header is missing:");
            return Response.status(Response.Status.UNAUTHORIZED).entity(
                    Map.of("message", "Authorization header is missing")
            ).build();
        }
    }
}
