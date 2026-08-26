package me.dinuka.gtlc.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.ejb.AdminSessonBean;
import me.dinuka.gtlc.util.JwtUtil;

import java.util.Map;

@RequestScoped
public class AdminService {

    @Inject
    private AdminSessonBean adminSessionBean;

    public Response saveNewUser(String authHeader, Map<String, Object> userMap){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String username = jwt.getSubject();

                String saved = adminSessionBean.saveNewUser(userMap);

                return Response.status(Response.Status.OK)
                        .entity(saved)
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

    public Response getAllUsers(String authHeader){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String username = jwt.getSubject();

                String allUsers = adminSessionBean.getAllUsers();

                return Response.status(Response.Status.OK)
                        .entity(allUsers)
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
