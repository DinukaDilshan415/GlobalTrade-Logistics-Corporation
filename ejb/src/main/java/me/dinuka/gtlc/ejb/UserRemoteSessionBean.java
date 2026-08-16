package me.dinuka.gtlc.ejb;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.gtlc.dto.UserDTO;
import me.dinuka.gtlc.entity.AccountType;
import me.dinuka.gtlc.entity.User;
import me.dinuka.gtlc.remote.UserRemoteService;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

@Stateless
public class UserRemoteSessionBean implements UserRemoteService {

    @PersistenceContext(unitName = "gtlcPU")
    private EntityManager em;

    @Override
    public String register(UserDTO userDTO) {

        List<User> userList = em.createNamedQuery("User.findByEmail", User.class)
                .setParameter("email", userDTO.getEmail())
                .getResultList();

        if(!userList.isEmpty()){
            return "Email already exists";
        } else {
            AccountType type = em.createNamedQuery("AccountType.findByType", AccountType.class)
                    .setParameter("type", userDTO.getAccountType())
                    .getSingleResult();

            String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt(12));

            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setPasswordHash(hashedPassword);
            user.setAccountType(type);

            em.persist(user);
            return "success";
        }
    }
}
