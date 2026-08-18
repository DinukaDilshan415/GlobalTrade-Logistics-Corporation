package me.dinuka.gtlc.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.dto.AdminDTO;
import me.dinuka.gtlc.dto.UserDTO;
import me.dinuka.gtlc.ejb.RefreshTokenService;
import me.dinuka.gtlc.ejb.UserAuthService;
import me.dinuka.gtlc.entity.RefreshToken;
import me.dinuka.gtlc.util.JwtUtil;
import me.dinuka.gtlc.util.RegexValidator;

import java.util.*;

@RequestScoped
public class AuthService {

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Inject
    private UserAuthService loginService;

    @Inject
    private RefreshTokenService refreshTokenService;

    public Response userAuthService(UserDTO dto){

        String email = dto.getEmail();
        String password = dto.getPassword();

        if(email.isEmpty()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of(
                            "message", "Email is required"
                    ))
                    .build();
        }  else if(password.isEmpty()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of(
                            "message", "Password is required"
                    ))
                    .build();
        } else {

            UsernamePasswordCredential credential =
                    new UsernamePasswordCredential(email, password);

            CredentialValidationResult result = identityStoreHandler.validate(credential);

            if (result.getStatus() == CredentialValidationResult.Status.VALID) {
                String token = JwtUtil.generateToken(
                        result.getCallerPrincipal().getName(),
                        result.getCallerGroups()
                );

                RefreshToken refreshToken = refreshTokenService.create(result.getCallerPrincipal().getName());

                System.out.println("Token: " + token + " Refresh Token: " + refreshToken.getToken() + "");
                System.out.println("Email: " + result.getCallerPrincipal().getName() + " Roles: " + result.getCallerGroups());

                NewCookie refreshCookie = new NewCookie.Builder("refreshToken")
                        .value(refreshToken.getToken())
                        .path("/")   // Changed from "/auth/refresh"
                        .maxAge(60 * 60 * 24 * 7) // 7 days
                        .secure(false)  // Set to true only in production with HTTPS
                        .httpOnly(true)
                        .sameSite(NewCookie.SameSite.LAX)  // Changed from STRICT
                        .build();

                return Response.status(Response.Status.OK)
                        .entity(Map.of(
                                "message", "Login successful",
                                "accessToken", token,
                                "refreshToken", refreshToken.getToken(),
                                "email", result.getCallerPrincipal().getName(),
                                "roles", result.getCallerGroups()))
                        .cookie(refreshCookie)
                        .build();
            }

            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(
                            Map.of("error", "Invalid username or password")
                    ).build();
        }
    }

    public Response adminAuthService(AdminDTO dto){
        String username = dto.getUsername();
        String password = dto.getPassword();

        if(username.isEmpty()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of(
                            "message", "Username is required"
                    ))
                    .build();
        }  else if(password.isEmpty()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of(
                            "message", "Password is required"
                    ))
                    .build();
        } else {
            UsernamePasswordCredential credential =
                    new UsernamePasswordCredential(username, password);

            CredentialValidationResult result = identityStoreHandler.validate(credential);

            if (result.getStatus() == CredentialValidationResult.Status.VALID) {
                String token = JwtUtil.generateToken(
                        result.getCallerPrincipal().getName(),
                        result.getCallerGroups()
                );

                RefreshToken refreshToken = refreshTokenService.create(result.getCallerPrincipal().getName());

                System.out.println("Token: " + token + " Refresh Token: " + refreshToken.getToken() + "");
                System.out.println("username: " + result.getCallerPrincipal().getName() + " Roles: " + result.getCallerGroups());

                NewCookie refreshCookie = new NewCookie.Builder("refreshToken")
                        .value(refreshToken.getToken())
                        .path("/")   // Changed from "/auth/refresh"
                        .maxAge(60 * 60 * 24 * 7) // 7 days
                        .secure(false)  // Set to true only in production with HTTPS
                        .httpOnly(true)
                        .sameSite(NewCookie.SameSite.LAX)  // Changed from STRICT
                        .build();

                return Response.status(Response.Status.OK)
                        .entity(Map.of(
                                "message", "Login successful",
                                "accessToken", token,
                                "refreshToken", refreshToken.getToken(),
                                "username", result.getCallerPrincipal().getName(),
                                "roles", result.getCallerGroups()))
                        .cookie(refreshCookie)
                        .build();
            }
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(
                            Map.of("error", "Invalid username or password")
                    ).build();
        }
    }

    public Response refreshToken(String refreshToken){
        if (refreshToken == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of(
                            "message",
                            "Missing refresh token"))
                    .build();
        }

        Optional<RefreshToken> tokenOptional = refreshTokenService.findValid(refreshToken);
        if (tokenOptional.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(
                    Map.of(
                            "error", "Invalid or expired refresh token")
            ).build();
        }

        RefreshToken oldToken = tokenOptional.get();
        String email = oldToken.getemail();

        refreshTokenService.deleteToken(oldToken.getToken());
        RefreshToken newRefreshToken = refreshTokenService.create(email);

        Set<String> roles = loginService.getRoles(email);

        String token = JwtUtil.generateToken(email, roles);

        NewCookie refreshCookie = new NewCookie.Builder("refreshToken")
                .value(newRefreshToken.getToken())
                .path("/")   // Changed from "/auth/refresh"
                .maxAge(60 * 60 * 24 * 7) // 7 days
                .secure(false)  // Set to true only in production with HTTPS
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.LAX)  // Changed from STRICT
                .build();

        return Response.status(Response.Status.OK)
                .entity(
                        Map.of(
                                "accessToken", token,
                                "refreshToken", newRefreshToken.getToken(),
                                "email", email,
                                "roles", roles))
                .cookie(refreshCookie)
                .build();
    }

    public Response logout(String token){
        if (token != null) {

            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);

                String email = jwt.getSubject();

                RefreshToken refreshToken = refreshTokenService.findTokenByUsername(email);
                refreshTokenService.deleteToken(refreshToken.getToken());
            }
        }
        return Response.status(Response.Status.OK)
                .entity(Map.of(
                        "message", "Logged Out")
                ).build();
    }
}
