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
@Transactional
public class RefreshTokenService {
    private static final long REFRESH_TOKEN_VALIDITY_DAYS = 7;

    @PersistenceContext(unitName = "gtlcPU")
    private EntityManager em;

    public RefreshToken create(String username) {
        deleteTokenByUsername(username);

        String token = UUID.randomUUID().toString().replace("-", "")
                + UUID.randomUUID().toString().replace("-", "");

        Instant expiry = Instant.now().plusSeconds(REFRESH_TOKEN_VALIDITY_DAYS * 24 * 3600);
        RefreshToken rt = new RefreshToken(token, username, expiry);

        em.persist(rt);
        return rt;
    }

    public Optional<RefreshToken> findValid(String token) {
        return em.createNamedQuery("RefreshToken.findByValidToken", RefreshToken.class)
                .setParameter("token", token)
                .setParameter("now", Instant.now())
                .getResultStream().findFirst();
    }

    public void deleteToken(String token) {
        em.createNamedQuery("RefreshToken.deleteToken")
                .setParameter("token", token)
                .executeUpdate();
    }

    private void deleteTokenByUsername(String username) {
        em.createNamedQuery("RefreshToken.deleteByUsername")
                .setParameter("username", username)
                .executeUpdate();
    }

}
