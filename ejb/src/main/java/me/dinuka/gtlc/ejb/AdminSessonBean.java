package me.dinuka.gtlc.ejb;

import com.google.gson.Gson;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.gtlc.entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class AdminSessonBean {
    Gson gson = new Gson();
    @PersistenceContext(unitName = "gtlcPU")
    private EntityManager em;

    public String saveNewUser(Map<String, Object> userMap){
        Map<String, Object> roleDetails = (Map<String, Object>) userMap.get("roleDetails");

        String countryId = (String) roleDetails.get("country");
        String position = (String) roleDetails.get("position");
        String regNumber = (String) roleDetails.get("reg_number");
        String fullName = (String) userMap.get("fullName");
        String username = (String) userMap.get("username");
        String role = (String) userMap.get("role");

        Country country = em.createNamedQuery("Country.findById", Country.class).setParameter("id", Integer.valueOf(countryId)).getSingleResult();
        UserStatus status = em.createNamedQuery("UserStatus.findByStatusId", UserStatus.class).setParameter("id", 1).getSingleResult();
        User user = em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username).getSingleResult();

        if(role.equals("admin")){
            return gson.toJson(Map.of(
                    "status", false,
                    "message", "Admin feature is not available yet"
            ));
        } else if(role.equals("customs agent")) {
            List<CustomAgent> resultList = em.createNamedQuery("CustomAgent.findByUser", CustomAgent.class).setParameter("user", user).getResultList();

            if (!resultList.isEmpty()){
                return gson.toJson(Map.of(
                        "status", false,
                        "message", "Customs Agent already exists"
                ));
            } else {
                CustomAgent customAgent = new CustomAgent();
                customAgent.setName(fullName);
                customAgent.setPosition(position);
                customAgent.setRegNumber(regNumber);
                customAgent.setCountry(country);
                customAgent.setUser(user);
                customAgent.setUserStatus(status);
                customAgent.setCreatedAt(java.time.LocalDateTime.now());
                customAgent.setUpdatedAt(java.time.LocalDateTime.now());
                em.persist(customAgent);

                return gson.toJson(Map.of(
                        "status", true,
                        "message", "Customs Agent created"
                ));
            }
        } else if(role.equals("logistics coordinator")){
            return gson.toJson(Map.of(
                    "status", false,
                    "message", "Logistics Coordinator feature is not available yet"
            ));
        } else {
            return gson.toJson(Map.of(
                    "status", false,
                    "message", "Invalid role"
            ));
        }
    }

    public String getAllUsers(){
        List<AdminProfile> adminProfileList = em.createNamedQuery("AdminProfile.findAll", AdminProfile.class).getResultList();
        List<CustomAgent> customAgentList = em.createNamedQuery("CustomAgent.findAll", CustomAgent.class).getResultList();

        if(adminProfileList.isEmpty() && customAgentList.isEmpty()){
            return gson.toJson(Map.of(
                    "status", false,
                    "message", "No Users Found"
            ));
        }

        ArrayList<HashMap<String, Object>> usersMapList = new ArrayList<>();

        if (!adminProfileList.isEmpty()){
            for (AdminProfile adminProfile : adminProfileList) {
                HashMap<String, Object> adminMap = new HashMap<>();
                adminMap.put("id", "AD-"+adminProfile.getId());
                adminMap.put("fullName", adminProfile.getName());
                adminMap.put("username", adminProfile.getAdmin().getUsername());
                adminMap.put("role", "admin");
                adminMap.put("status", adminProfile.getUserStatus().getStatus());
                adminMap.put("roleDetails", Map.of());
                usersMapList.add(adminMap);
            }
        }

        if (!customAgentList.isEmpty()){
            for (CustomAgent customAgent : customAgentList){
                HashMap<String, Object> customMap = new HashMap<>();
                customMap.put("id", "CA-"+customAgent.getId());
                customMap.put("fullName", customAgent.getName());
                customMap.put("username", customAgent.getUser().getUsername());
                customMap.put("role", "customs agent");
                customMap.put("status", customAgent.getUserStatus().getStatus());
                customMap.put("roleDetails", Map.of(
                        "position", customAgent.getPosition(),
                        "reg_number", customAgent.getRegNumber(),
                        "country", customAgent.getCountry().getId()
                ));
                usersMapList.add(customMap);
            }
        }

        return gson.toJson(Map.of(
                "status", true,
                "data", usersMapList
        ));
    }
}
