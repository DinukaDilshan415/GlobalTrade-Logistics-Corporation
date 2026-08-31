package me.dinuka.gtlc.ejb;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.gtlc.annotation.Logged;
import me.dinuka.gtlc.entity.Admin;
import me.dinuka.gtlc.entity.User;
import me.dinuka.gtlc.log.ApplicationLogger;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@RequestScoped
public class AdminAuthService {

    @PersistenceContext(unitName = "gtlcPU")
    private EntityManager em;

    private static final Logger LOGGER = ApplicationLogger.getLogger();

    public Optional<Admin> getUser(String username) {
        try {
            return Optional.of(em.createNamedQuery("Admin.findByUsername", Admin.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Logged
    public boolean validate(String username, String password) {
        LOGGER.info("Validating admin credentials for: " + username);
        return getUser(username)
                .map(admin -> BCrypt.checkpw(password, admin.getPasswordHash()))
                .orElse(false);
    }

    public Set<String> getRoles(String username) {
        return getUser(username)
                .map(admin -> admin.getRoles().getRole())
                .map(String::toLowerCase)
                .map(Collections::singleton)
                .orElse(Collections.emptySet());
    }

}
