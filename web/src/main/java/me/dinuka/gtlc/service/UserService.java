package me.dinuka.gtlc.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.dto.UserDTO;
import me.dinuka.gtlc.remote.UserRemoteService;
import me.dinuka.gtlc.util.JwtUtil;
import me.dinuka.gtlc.util.RegexValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Map;

@RequestScoped
public class UserService {

    @EJB
    private UserRemoteService userRemoteService;

    Gson gson = new Gson();
    public Response registerUser(UserDTO dto){

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", false);

        String username = dto.getUsername();
        String email = dto.getEmail();
        String accountType = dto.getAccountType();
        String password = dto.getPassword();

        if(username.isEmpty()){
            jsonObject.addProperty("message", "Username is required");
        } else if(email.isEmpty()){
            jsonObject.addProperty("message", "Email is required");
        } else if(accountType == null){
            jsonObject.addProperty("message", "Account type is required");
        } else if(password.isEmpty()){
            jsonObject.addProperty("message", "Password is required");
        } else if (!RegexValidator.isValidPassword(password)) {
            jsonObject.addProperty("message", "The Password must contain at least "
                    + "uppercase, lowercase, numbers, special character and to be 8 characters long !");
        } else {
            String register = userRemoteService.register(dto);
            if(register.equals("success")){
                jsonObject.addProperty("status", true);
                jsonObject.addProperty("message", "Registration successful");
                return Response.ok(gson.toJson(jsonObject)).build();
            } else {
                jsonObject.addProperty("message", register);
            }
        }

        return Response.ok(gson.toJson(jsonObject)).build();
    }

    public Response getAll(String authHeader){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String username = jwt.getSubject();

                String allUsers = userRemoteService.getAllUsers();

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
