package me.dinuka.gtlc.ejb;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.gtlc.entity.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@RequestScoped
public class UserAuthService {

    @PersistenceContext(unitName = "gtlcPU")
    private EntityManager em;

    public Optional<User> getUser(String email) {
        try {
            return Optional.of(em.createNamedQuery("User.findByEmail", User.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean validate(String email, String password) {
        return getUser(email)
                .map(user -> BCrypt.checkpw(password, user.getPasswordHash()))
                .orElse(false);
    }

    public Set<String> getRoles(String email) {
        return getUser(email)
                .map(user -> user.getAccountType().getType())
                .map(String::toLowerCase)
                .map(Collections::singleton)
                .orElse(Collections.emptySet());
    }

}
