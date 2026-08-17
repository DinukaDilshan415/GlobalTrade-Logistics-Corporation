package me.dinuka.gtlc.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.dto.AdminDTO;
import me.dinuka.gtlc.dto.UserDTO;
import me.dinuka.gtlc.service.AuthService;

@Path("/auth")
public class AuthController {

    @Inject
    private AuthService authService;

    @POST
    @Path("/user/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogin(UserDTO body){
        return authService.userAuthService(body);
    }

    @POST
    @Path("/admin/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adminLogin(AdminDTO body){
        return authService.adminAuthService(body);
    }

    @POST
    @Path("/refresh")
    public Response refresh(String refreshToken){
        return authService.refreshToken(refreshToken);
    }

    @POST
    @Path("/logout")
    public Response logout(String refreshToken){
        return authService.logout(refreshToken);
    }
}
