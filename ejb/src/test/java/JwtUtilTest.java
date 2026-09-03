import com.auth0.jwt.interfaces.DecodedJWT;
import me.dinuka.gtlc.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtUtil Tests")
class JwtUtilTest {

    @Test
    @DisplayName("Should generate valid JWT token with email and roles")
    void testGenerateToken() {
        String email = "doris@gmail.com";
        Set<String> roles = Set.of("customer", "admin");
        
        String token = JwtUtil.generateToken(email, roles);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(JwtUtil.isValid(token));
    }

    @Test
    @DisplayName("Should generate different tokens for same input")
    void testGenerateTokenDifferent() {
        String email = "doris@gmail.com";
        Set<String> roles = Set.of("customer");
        
        String token1 = JwtUtil.generateToken(email, roles);
        String token2 = JwtUtil.generateToken(email, roles);
        
        assertNotEquals(token1, token2);
    }

    @Test
    @DisplayName("Should validate correct JWT token")
    void testIsValidWithValidToken() {
        String token = JwtUtil.generateToken("doris@gmail.com", Set.of("customer"));

        boolean isValid = JwtUtil.isValid(token);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should reject invalid JWT token")
    void testIsValidWithInvalidToken() {
        String invalidToken = "invalid.jwt.token";

        boolean isValid = JwtUtil.isValid(invalidToken);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false for malformed token")
    void testIsValidWithMalformedToken() {
        boolean isValid = JwtUtil.isValid("malformed");

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should parse valid token and extract subject")
    void testParseTokenAndGetSubject() {
        String email = "doris@gmail.com";
        String token = JwtUtil.generateToken(email, Set.of("customer"));

        DecodedJWT decodedJWT = JwtUtil.parseToken(token);

        assertNotNull(decodedJWT);
        assertEquals(email, decodedJWT.getSubject());
    }

    @Test
    @DisplayName("Should extract email from token")
    void testGetEmailFromToken() {
        String email = "doris@gmail.com";
        String token = JwtUtil.generateToken(email, Set.of("customer"));

        String extractedEmail = JwtUtil.getEmail(token);

        assertEquals(email, extractedEmail);
    }

    @Test
    @DisplayName("Should extract roles from token")
    void testGetRolesFromToken() {
        String email = "doris@gmail.com";
        Set<String> roles = Set.of("customer", "admin", "MANAGER");
        String token = JwtUtil.generateToken(email, roles);

        Set<String> extractedRoles = JwtUtil.getRoles(token);

        assertEquals(roles, extractedRoles);
    }

    @Test
    @DisplayName("Should return empty set when no roles in token")
    void testGetRolesReturnsEmptySetWhenNoRoles() {
        String email = "doris@gmail.com";
        String token = JwtUtil.generateToken(email, Set.of());

        Set<String> extractedRoles = JwtUtil.getRoles(token);

        assertTrue(extractedRoles.isEmpty());
    }

    @Test
    @DisplayName("Should throw exception when parsing invalid token")
    void testParseInvalidTokenThrowsException() {
        String invalidToken = "invalid.token";

        assertThrows(
                Exception.class,
                () -> JwtUtil.parseToken(invalidToken),
                "Should throw exception for invalid token"
        );
    }

    @Test
    @DisplayName("Should maintain token integrity")
    void testTokenIntegrity() {
        String email = "doris@gmail.com";
        Set<String> roles = Set.of("customer");
        String token = JwtUtil.generateToken(email, roles);

        String parsedEmail = JwtUtil.getEmail(token);
        Set<String> parsedRoles = JwtUtil.getRoles(token);

        assertEquals(email, parsedEmail);
        assertEquals(roles, parsedRoles);
    }

    @Test
    @DisplayName("Should handle multiple roles correctly")
    void testMultipleRolesInToken() {
        String email = "admin123";
        Set<String> roles = Set.of("admin", "customer", "customs_agent", "manager");

        String token = JwtUtil.generateToken(email, roles);
        Set<String> extractedRoles = JwtUtil.getRoles(token);

        assertEquals(4, extractedRoles.size());
        assertTrue(extractedRoles.contains("admin"));
        assertTrue(extractedRoles.contains("customer"));
        assertTrue(extractedRoles.contains("MANAGER"));
        assertTrue(extractedRoles.contains("SUPERVISOR"));
    }
}
