package me.dinuka.gtlc.ejb;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import me.dinuka.gtlc.dto.vendorDTO;
import me.dinuka.gtlc.entity.Country;
import me.dinuka.gtlc.entity.User;
import me.dinuka.gtlc.entity.Vendor;
import me.dinuka.gtlc.entity.VendorStatus;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Stateless
public class VendorSessionBean {

    @PersistenceContext(unitName = "gtlcPU")
    private EntityManager em;

    Gson gson = new Gson();

    public String vendorProfileChecker(String email) {
        JsonObject jsonObject = new JsonObject();

        try {
            User user = em.createNamedQuery("User.findByEmail", User.class)
                    .setParameter("email", email)
                    .getSingleResult();

            Vendor vendor = em.createNamedQuery("Vendor.findByUser", Vendor.class)
                    .setParameter("user", user)
                    .getSingleResult();

            jsonObject.addProperty("hasAccount", true);
            jsonObject.addProperty("vendorId", vendor.getVendorIdString());
            jsonObject.addProperty("companyName", vendor.getCompanyName());
            jsonObject.addProperty("contactPerson", vendor.getContactPerson());
            jsonObject.addProperty("email", vendor.getEmail());
            jsonObject.addProperty("phone", vendor.getPhone());
            jsonObject.addProperty("address", vendor.getAddress());
            jsonObject.addProperty("regNumber", vendor.getRegNumber());
            jsonObject.addProperty("complianceInformation", vendor.getComplianceInformation());
            jsonObject.addProperty("reqDate", vendor.getReqDate().toString());

            String formatted = vendor.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy", java.util.Locale.ENGLISH));

            jsonObject.addProperty("createdAt", formatted);
            jsonObject.addProperty("country", vendor.getCountry().getName());
            jsonObject.addProperty("status", vendor.getVendorStatus().getStatus());

        } catch (NoResultException e) {
            System.out.println("No Result Found: " + e.getMessage() + "");
            jsonObject.addProperty("hasAccount", false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return gson.toJson(jsonObject);
    }

    public String saveVendorAccountOpenRequest(String email, vendorDTO dto){
        try {
            User user = em.createNamedQuery("User.findByEmail", User.class)
                    .setParameter("email", email)
                    .getSingleResult();

            List<Vendor> vendors = em.createNamedQuery("Vendor.findByUser", Vendor.class).setParameter("user", user).getResultList();

            if(!vendors.isEmpty()){
                return "Vendor account already exists";
            } else {
                List<Vendor> resultList = em.createNamedQuery("Vendor.findByVendorId", Vendor.class)
                        .setParameter("vendorIdString", dto.getVendorId())
                        .getResultList();

                if(!resultList.isEmpty()){
                    return "Vendor ID already exists. Try Again";
                } else {
                    VendorStatus vendorStatus = em.createNamedQuery("VendorStatus.findByStatusId", VendorStatus.class).setParameter("id", 1).getSingleResult();
                    Country country = em.createNamedQuery("Country.findById", Country.class).setParameter("id", Integer.valueOf(dto.getCountry())).getSingleResult();

                    Vendor vendor = new Vendor();
                    vendor.setUser(user);
                    vendor.setVendorIdString(dto.getVendorId());
                    vendor.setCompanyName(dto.getCompanyName());
                    vendor.setContactPerson(dto.getContactPerson());
                    vendor.setEmail(dto.getEmail());
                    vendor.setPhone(dto.getPhone());
                    vendor.setAddress(dto.getAddress());
                    vendor.setRegNumber(dto.getRegistrationNumber());
                    vendor.setComplianceInformation(dto.getComplianceInfo());
                    vendor.setVendorStatus(vendorStatus);
                    vendor.setCountry(country);
                    vendor.setReqDate(java.time.LocalDateTime.now());
                    vendor.setCreatedAt(java.time.LocalDateTime.now());

                    em.persist(vendor);

                    return "success";
                }
            }

        } catch (NoResultException e) {
            return "User not found";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
