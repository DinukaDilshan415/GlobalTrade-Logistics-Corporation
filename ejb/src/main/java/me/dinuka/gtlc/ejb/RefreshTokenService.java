package me.dinuka.gtlc.ejb;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import me.dinuka.gtlc.entity.RefreshToken;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class RefreshTokenService {
    private static final long REFRESH_TOKEN_VALIDITY_DAYS = 7;
    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_BACKOFF_MS = 50;

    @PersistenceContext(unitName = "gtlcPU")
    private EntityManager em;

    @Transactional
    public RefreshToken create(String username) {
        return createWithRetry(username, 0);
    }

    private RefreshToken createWithRetry(String username, int attempt) {
        try {
            deleteTokenByUsername(username);

            String token = UUID.randomUUID().toString().replace("-", "")
                    + UUID.randomUUID().toString().replace("-", "");

            Instant expiry = Instant.now().plusSeconds(REFRESH_TOKEN_VALIDITY_DAYS * 24 * 3600);
            RefreshToken rt = new RefreshToken(token, username, expiry);

            em.persist(rt);
            em.flush(); // Ensure immediate execution to catch deadlocks
            return rt;
        } catch (Exception e) {
            if (e.getCause() != null &&
                    e.getCause().getMessage() != null &&
                    e.getCause().getMessage().contains("Deadlock") &&
                    attempt < MAX_RETRIES) {

                long backoffTime = INITIAL_BACKOFF_MS * (long) Math.pow(2, attempt);
                try {
                    Thread.sleep(backoffTime);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Token creation interrupted", ie);
                }

                return createWithRetry(username, attempt + 1);
            }
            throw new RuntimeException("Failed to create refresh token after " + MAX_RETRIES + " attempts", e);
        }
    }

    @Transactional
    public Optional<RefreshToken> findValid(String token) {
        return em.createNamedQuery("RefreshToken.findByValidToken", RefreshToken.class)
                .setParameter("token", token)
                .setParameter("now", Instant.now())
                .getResultStream().findFirst();
    }

    @Transactional
    public void deleteToken(String token) {
        em.createNamedQuery("RefreshToken.deleteToken")
                .setParameter("token", token)
                .executeUpdate();
    }

    @Transactional
    public void deleteTokenByUsername(String username) {
        em.createNamedQuery("RefreshToken.deleteByUsername")
                .setParameter("username", username)
                .executeUpdate();
    }

    @Transactional
    public RefreshToken findTokenByUsername(String username){
        return em.createNamedQuery("RefreshToken.findByEmail", RefreshToken.class)
                .setParameter("email", username)
                .getSingleResult();
    }
}