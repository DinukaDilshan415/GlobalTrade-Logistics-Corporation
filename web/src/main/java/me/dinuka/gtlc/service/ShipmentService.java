package me.dinuka.gtlc.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.ejb.ShipmentSessionBean;
import me.dinuka.gtlc.util.JwtUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequestScoped
public class ShipmentService {

    @Inject
    private ShipmentSessionBean shipmentSessionBean;

    public Response getAllPendingShipmets(String authHeader){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String username = jwt.getSubject();

                ArrayList<HashMap<String, Object>> shipments = shipmentSessionBean.getAllPendingShipments();

                if (shipments.isEmpty()) {
                    return Response.status(Response.Status.OK)
                            .entity(Map.of(
                                    "message", "No shipments found",
                                    "status", false))
                            .build();
                } else {
                    return Response.status(Response.Status.OK)
                            .entity(Map.of(
                                    "message", "Shipments retrieved successfully",
                                    "status", true,
                                    "data", shipments))
                            .build();
                }
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

    public Response getAllActiveShipments(String authHeader){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String username = jwt.getSubject();

                ArrayList<HashMap<String, Object>> shipments = shipmentSessionBean.getAllActiveShipments();

                if (shipments.isEmpty()) {
                    return Response.status(Response.Status.OK)
                            .entity(Map.of(
                                    "message", "No active shipments found",
                                    "status", false))
                            .build();
                } else {
                    return Response.status(Response.Status.OK)
                            .entity(Map.of(
                                    "message", "Active shipments retrieved successfully",
                                    "status", true,
                                    "data", shipments))
                            .build();
                }
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

    public Response updatePendingShipment(String authHeader, Map<String, String> body){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String username = jwt.getSubject();

                String shipment_id = body.get("shipment_id");
                String category = body.get("category");
                String status = body.get("status");

                String updated = shipmentSessionBean.updatePendingShipment(shipment_id, category, status);

                return Response.status(Response.Status.OK)
                        .entity(Map.of("message", updated))
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

    public Response updateProgress(String authHeader, Map<String, String> body){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String username = jwt.getSubject();

                String shipment_id = body.get("shipment_id");
                String status = body.get("status");
                String location = body.get("location");
                String description = body.get("description");

                String updated = shipmentSessionBean.updateProgress(shipment_id, status, location, description);

                return Response.status(Response.Status.OK)
                        .entity(updated)
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
