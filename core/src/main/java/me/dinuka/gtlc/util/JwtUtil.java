package me.dinuka.gtlc.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class JwtUtil {
    private static final String SECRET = "50e084eebe252b42db381c61b237853a962d5c5b137676e00098989e6948711a";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    private static final long EXPIRATION_SECOND = 3600;

    private static final JWTVerifier VERIFIER = JWT.require(ALGORITHM).build();

    public static String generateToken(String email, Set<String> roles) {
        Instant now = Instant.now();

        return JWT.create()
                .withSubject(email)
                .withClaim("roles", List.copyOf(roles))
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusSeconds(EXPIRATION_SECOND)))
                .sign(ALGORITHM);
    }

    public static DecodedJWT parseToken(String token) {
        return VERIFIER.verify(token);
    }

    public static boolean isValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public static String getEmail(String token) {
        return parseToken(token).getSubject();
    }

    public static Set<String> getRoles(String token) {
        List<String> roles = parseToken(token).getClaim("roles").asList(String.class);
        return roles != null ? Set.copyOf(roles) : Set.of();
    }


}
