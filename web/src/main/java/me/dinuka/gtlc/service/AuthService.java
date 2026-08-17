package me.dinuka.gtlc.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.dto.UserDTO;
import me.dinuka.gtlc.ejb.RefreshTokenService;
import me.dinuka.gtlc.ejb.UserAuthService;
import me.dinuka.gtlc.entity.RefreshToken;
import me.dinuka.gtlc.util.JwtUtil;
import me.dinuka.gtlc.util.RegexValidator;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

                return Response.status(Response.Status.OK)
                        .entity(Map.of(
                                "message", "Login successful",
                                "accessToken", token,
                                "refreshToken", refreshToken.getToken(),
                                "email", result.getCallerPrincipal().getName(),
                                "roles", result.getCallerGroups()))
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

        return Response.status(Response.Status.OK)
                .entity(
                        Map.of(
                                "accessToken", token,
                                "refreshToken", newRefreshToken.getToken(),
                                "email", email,
                                "roles", roles))
                .build();
    }

    public Response logout(String refreshToken){
        if (refreshToken != null) {
            refreshTokenService.deleteToken(refreshToken);
        }
        return Response.status(Response.Status.OK)
                .entity(Map.of(
                        "message", "Logged Out")
                ).build();
    }
}
