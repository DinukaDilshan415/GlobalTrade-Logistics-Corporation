package me.dinuka.gtlc.ejb;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import me.dinuka.gtlc.dto.ProductDTO;
import me.dinuka.gtlc.dto.vendorDTO;
import me.dinuka.gtlc.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String getShipments(String email){
        User user = em.createNamedQuery("User.findByEmail", User.class)
                .setParameter("email", email)
                .getSingleResult();

        Vendor vendor = em.createNamedQuery("Vendor.findByUser", Vendor.class)
                .setParameter("user", user)
                .getSingleResult();

        List<VendorShipment> shipments = em.createNamedQuery("VendorShipment.findByVendor", VendorShipment.class).setParameter("vendor", vendor).getResultList();

        ArrayList<Map<String, Object>> shipmentList = new ArrayList<>();

        for(VendorShipment shipment : shipments){
            HashMap<String, Object> shipmentMap = new HashMap<>();
            shipmentMap.put("id", shipment.getShipmentIdString());
            shipmentMap.put("category", shipment.getShipCategory().getName());
            shipmentMap.put("destination", shipment.getWarehouse() != null ? shipment.getWarehouse().getName() : shipment.getDestCountry().getName() + " - " + shipment.getDestAddress());
            shipmentMap.put("carrier", shipment.getCarrier());
            shipmentMap.put("expectedDate", shipment.getExpectData().format(DateTimeFormatter.ofPattern("MMM dd, yyyy", java.util.Locale.ENGLISH)));
            shipmentMap.put("status", shipment.getShipStatus().getName());
            shipmentMap.put("weight", shipment.getWeight()+" kg");
            shipmentList.add(shipmentMap);
        }

        return gson.toJson(Map.of(
                "shipments", shipmentList
        ));
    }

    public String getAllVendors(){
        List<Vendor> resultList = em.createNamedQuery("Vendor.findAll", Vendor.class).getResultList();

        if(resultList.isEmpty()){
            return "No Vendors Found";
        } else {
            ArrayList<vendorDTO> vendorList = new ArrayList<>();
            for(Vendor vendor : resultList){
                vendorDTO dto = new vendorDTO();
                dto.setVendorId(vendor.getVendorIdString());
                dto.setCompanyName(vendor.getCompanyName());
                dto.setContactPerson(vendor.getContactPerson());
                dto.setEmail(vendor.getEmail());
                dto.setPhone(vendor.getPhone());
                dto.setAddress(vendor.getAddress());
                dto.setCountry(vendor.getCountry().getName());
                dto.setRegistrationNumber(vendor.getRegNumber());
                dto.setComplianceInfo(vendor.getComplianceInformation());
                dto.setStatus(vendor.getVendorStatus().getStatus());

                vendorList.add(dto);
            }
            return gson.toJson(vendorList);
        }
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

    public String updateVendorStatus(String vendorId, String status){
        try {
            Vendor vendor = em.createNamedQuery("Vendor.findByVendorId", Vendor.class).setParameter("vendorIdString", vendorId).getSingleResult();
            VendorStatus vendorStatus = em.createNamedQuery("VendorStatus.findByStatus", VendorStatus.class).setParameter("status", status).getSingleResult();

            vendor.setVendorStatus(vendorStatus);
            em.merge(vendor);

            return "Vender Status : " + status + " Updated";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String saveShipment(String email, Map<String, Object> body){
        User user = em.createNamedQuery("User.findByEmail", User.class)
                .setParameter("email", email)
                .getSingleResult();

        Vendor vendor = em.createNamedQuery("Vendor.findByUser", Vendor.class)
                .setParameter("user", user)
                .getSingleResult();

        if (!vendor.getVendorStatus().getStatus().equals("active")){
            return gson.toJson(Map.of(
                    "status", false,
                    "message", "Vendor Account is under review. Please wait for approval"
            ));
        }

        ShipStatus shipStatus = em.createNamedQuery("ShipStatus.findById", ShipStatus.class).setParameter("id", 3).getSingleResult();

        String shipmentId = body.get("shipmentId").toString();

        List<VendorShipment> resultList = em.createNamedQuery("VendorShipment.findByShipmentIdString", VendorShipment.class).setParameter("shipmentIdString", shipmentId).getResultList();
        if(!resultList.isEmpty()){
            return gson.toJson(Map.of(
                    "status", false,
                    "message", "Shipment ID already exists. Try Again"));
        }

        String category = body.get("category").toString();
        Double weight = Double.parseDouble(body.get("weight").toString());
        String description = body.get("description").toString();
        String expectedDate = body.get("expectedDate").toString();
        String carrier = body.get("carrier").toString();
        String originAddress = body.get("originAddress").toString();
        int originCountryId = Integer.parseInt(body.get("originCountryId").toString());
        String destAddress = body.get("destAddress").toString();
        int destCountryId = Integer.parseInt(body.get("destCountryId").toString());
        String destWarehouseId = body.get("destWarehouseId").toString();
        String recipientName = body.get("recipientName").toString();
        String recipientPhone = body.get("recipientPhone").toString();
        String senderName = body.get("senderName").toString();
        String senderPhone = body.get("senderPhone").toString();

        ArrayList<ProductDTO> products = new ArrayList<>();
        ArrayList<Map<String, Object>> productMaps = (ArrayList<Map<String, Object>>) body.get("products");

        for (Map<String, Object> productMap : productMaps) {
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productMap.get("name").toString())
                    .hsCode(productMap.get("hsCode").toString())
                    .quantity(Integer.parseInt(productMap.get("quantity").toString()))
                    .unitValue(Integer.parseInt(productMap.get("unitValue").toString()))
                    .build();
            products.add(productDTO);
        }


        Country originCountry = em.createNamedQuery("Country.findById", Country.class).setParameter("id", originCountryId).getSingleResult();
        Country destCountry = em.createNamedQuery("Country.findById", Country.class).setParameter("id", destCountryId).getSingleResult();

        ShipCategory shipCategory = em.createNamedQuery("ShipCategory.findByName", ShipCategory.class).setParameter("name", category).getSingleResult();

        Warehouse destWarehouse = null;
        if(!destWarehouseId.isEmpty()){
            destWarehouse = em.createNamedQuery("Warehouse.findById", Warehouse.class).setParameter("id", Integer.parseInt(destWarehouseId)).getSingleResult();
        }

        LocalDateTime expectedDateFormatted = LocalDate.parse(expectedDate).atStartOfDay();

        VendorShipment vendorShipment = new VendorShipment();
        vendorShipment.setShipmentIdString(shipmentId);
        vendorShipment.setCarrier(carrier);
        vendorShipment.setExpectData(expectedDateFormatted);
        vendorShipment.setWeight(weight);
        vendorShipment.setDescription(description);
        vendorShipment.setOriginAddress(originAddress);
        vendorShipment.setOriginCountry(originCountry);
        vendorShipment.setSenderName(senderName);
        vendorShipment.setSenderPhone(senderPhone);
        vendorShipment.setDestAddress(destAddress);
        vendorShipment.setWarehouse(destWarehouse);
        vendorShipment.setRecipientName(recipientName);
        vendorShipment.setRecipientPhone(recipientPhone);
        vendorShipment.setShipCategory(shipCategory);
        vendorShipment.setVendor(vendor);
        vendorShipment.setDestCountry(destCountry);
        vendorShipment.setCreatedAt(LocalDateTime.now());
        vendorShipment.setShipStatus(shipStatus);

        em.persist(vendorShipment);

        for (ProductDTO productDTO : products) {
            ShipProduct product = new ShipProduct();
            product.setName(productDTO.getName());
            product.setHsCode(productDTO.getHsCode());
            product.setQuantity(productDTO.getQuantity());
            product.setUnitValue(productDTO.getUnitValue());
            product.setVendorShipment(vendorShipment);
            em.persist(product);
        }

        return gson.toJson(Map.of(
                "status", true,
                "message", "Shipment saved successfully",
                "newShipment", Map.of(
                        "id", shipmentId,
                        "category", category,
                        "destination", (!destWarehouseId.isEmpty() ? destWarehouse.getName() : destCountry.getName()+" - "+destAddress ),
                        "carrier", carrier,
                        "expectedDate", expectedDate,
                        "status", "PENDING",
                        "weight", weight + " kg"
                )
        ));
    }

}
