package me.dinuka.gtlc.ejb;

import com.google.gson.Gson;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import me.dinuka.gtlc.entity.*;

import java.time.LocalDate;
import java.util.*;

@Stateless
public class ShipmentSessionBean {
    @PersistenceContext(unitName = "gtlcPU")
    private EntityManager em;

    Gson gson = new Gson();

    public String getShipmentTracking(String shipment_id) {
        try {
            Shipment shipment = em.createNamedQuery("Shipment.findByShipmentId", Shipment.class)
                    .setParameter("shipmentIdString", shipment_id)
                    .getSingleResult();

            List<ShipmentProgress> shipmentProgresses = em.createNamedQuery("ShipmentProgress.findByShipmentDesc", ShipmentProgress.class)
                    .setParameter("shipment", shipment)
                    .getResultList();

            List<ShipmentItem> shipmentItemList = em.createNamedQuery("ShipmentItem.findByShipment", ShipmentItem.class)
                    .setParameter("shipment", shipment)
                    .getResultList();

            HashMap<String, Object> shipmentDetails = new HashMap<>();
            shipmentDetails.put("id", shipment.getShipmentIdString());
            shipmentDetails.put("status", shipmentProgresses.get(0).getShipStatus().getName());
            shipmentDetails.put("originCountry", shipment.getOriginCountry().getName());
            shipmentDetails.put("originAddress", shipment.getOriginAddress());
            shipmentDetails.put("destCountry", shipment.getDestCountry().getName());
            shipmentDetails.put("destAddress", shipment.getDestAddress());
            shipmentDetails.put("expectedDate", shipment.getExpectData()
                    .format(java.time.format.DateTimeFormatter
                            .ofPattern("MMM dd, yyyy", java.util.Locale.ENGLISH)));
            shipmentDetails.put("expectedTime", "");
            shipmentDetails.put("carrier", shipment.getCarrier());
            shipmentDetails.put("weight", shipment.getWeight() + " kg");
            shipmentDetails.put("category", "");
            shipmentDetails.put("totalItems", shipmentItemList.size());
            shipmentDetails.put("customsCleared", true);

            ArrayList<HashMap<String, Object>> shipmentProgressesList = new ArrayList<>();

            for (ShipmentProgress shipmentProgress : shipmentProgresses) {
                HashMap<String, Object> shipmentProgressMap = new HashMap<>();
                shipmentProgressMap.put("status", shipmentProgress.getShipStatus().getName());
                shipmentProgressMap.put("location", shipmentProgress.getLocation());
                shipmentProgressMap.put("description", shipmentProgress.getDescription());
                shipmentProgressMap.put("datetime", shipmentProgress.getCreatedAt()
                        .format(java.time.format.DateTimeFormatter
                                .ofPattern("MMM dd, yyyy HH:mm: a", java.util.Locale.ENGLISH)));
                shipmentProgressesList.add(shipmentProgressMap);
            }

            return gson.toJson(Map.of(
                    "status", true,
                    "shipmentDetails", shipmentDetails,
                    "shipmentProgresses", shipmentProgressesList
            ));

        } catch (NoResultException e) {
            e.printStackTrace();
            return gson.toJson(Map.of(
                    "status", false,
                    "message", "Invalid Shipment ID, Please try again"
            ));
        }
    }

    public ArrayList<HashMap<String, Object>> getAllPendingShipments() {

        List<VendorShipment> resultList = em.createNamedQuery("VendorShipment.findAllByStatusId", VendorShipment.class)
                .setParameter("statusId", 3)
                .getResultList();

        if (resultList.isEmpty()) {
            return new ArrayList<>();
        } else {
            ArrayList<HashMap<String, Object>> shipmentList = new ArrayList<>();

            for (VendorShipment shipment : resultList) {
                HashMap<String, Object> shipmentMap = new HashMap<>();

                shipmentMap.put("shipment_id", shipment.getShipmentIdString());
                shipmentMap.put("category", shipment.getShipCategory().getName());
                shipmentMap.put("status", shipment.getShipStatus().getName());
                shipmentMap.put("carrier", shipment.getCarrier());
                shipmentMap.put("expect_date", shipment.getExpectData().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy", java.util.Locale.ENGLISH)));
                shipmentMap.put("weight", shipment.getWeight() + " kg");
                shipmentMap.put("description", shipment.getDescription());
                shipmentMap.put("originCountry", shipment.getOriginCountry().getName());
                shipmentMap.put("originAddress", shipment.getOriginAddress());
                shipmentMap.put("destCountry", shipment.getDestCountry().getName());
                shipmentMap.put("destAddress", shipment.getDestAddress());
                shipmentMap.put("products", new ArrayList<>());

                shipmentList.add(shipmentMap);
            }

            return shipmentList;
        }
    }

    public ArrayList<HashMap<String, Object>> getAllActiveShipments() {
        List<Shipment> resultList = em.createNamedQuery("Shipment.findAllLatestFirst", Shipment.class).getResultList();

        if (resultList.isEmpty()) {
            return new ArrayList<>();
        } else {
            ArrayList<HashMap<String, Object>> shipmentList = new ArrayList<>();

            for (Shipment shipment : resultList) {
                HashMap<String, Object> shipmentMap = new HashMap<>();

                shipmentMap.put("shipment_id", shipment.getShipmentIdString());
                shipmentMap.put("category", "");
                List<ShipmentProgress> progressList = em.createNamedQuery("ShipmentProgress.findByShipment", ShipmentProgress.class)
                        .setParameter("shipment", shipment)
                        .getResultList();
                if (!progressList.isEmpty()) {
                    shipmentMap.put("status", progressList.get(progressList.size() - 1).getShipStatus().getName());
                }
                shipmentMap.put("carrier", shipment.getCarrier());
                shipmentMap.put("expect_date", shipment.getExpectData().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy", java.util.Locale.ENGLISH)));
                shipmentMap.put("weight", shipment.getWeight() + " kg");
                shipmentMap.put("description", shipment.getDescription());
                shipmentMap.put("originCountry", shipment.getOriginCountry().getName());
                shipmentMap.put("originAddress", shipment.getOriginAddress());
                shipmentMap.put("destCountry", shipment.getDestCountry().getName());
                shipmentMap.put("destAddress", shipment.getDestAddress());
                shipmentMap.put("products", new ArrayList<>());

                shipmentList.add(shipmentMap);
            }
            return shipmentList;
        }
    }

    public String updatePendingShipment(String username, String shipment_id, String category, String status) {

        Admin admin = em.createNamedQuery("Admin.findByUsername", Admin.class).setParameter("username", username).getSingleResult();

        VendorShipment vendorShipment = em.createNamedQuery("VendorShipment.findByShipmentId", VendorShipment.class)
                .setParameter("shipmentIdString", shipment_id)
                .getSingleResult();

        List<ShipProduct> shipProducts = em.createNamedQuery("ShipProduct.findByVendorShipment", ShipProduct.class)
                .setParameter("vendorShipment", vendorShipment)
                .getResultList();

        ShipStatus shipStatus = em.createNamedQuery("ShipStatus.findByName", ShipStatus.class).setParameter("name", status).getSingleResult();

        if (vendorShipment.getShipStatus().getId() == 3) {
            vendorShipment.setShipStatus(shipStatus);
            em.merge(vendorShipment);

            if (shipStatus.getName().equals("ACCEPTED")) {
                if (vendorShipment.getShipCategory().getName().equals("DIRECT")) {
                    Shipment shipment = new Shipment();
                    shipment.setShipmentIdString(vendorShipment.getShipmentIdString());
                    shipment.setCarrier(vendorShipment.getCarrier());
                    shipment.setExpectData(vendorShipment.getExpectData());
                    shipment.setWeight(vendorShipment.getWeight());
                    shipment.setDescription(vendorShipment.getDescription());
                    shipment.setOriginAddress(vendorShipment.getOriginAddress());
                    shipment.setSenderName(vendorShipment.getSenderName());
                    shipment.setSenderPhone(vendorShipment.getSenderPhone());
                    shipment.setDestAddress(vendorShipment.getDestAddress());
                    shipment.setRecipientName(vendorShipment.getRecipientName());
                    shipment.setRecipientPhone(vendorShipment.getSenderPhone());
                    shipment.setCreatedAt(java.time.LocalDateTime.now());
                    shipment.setUpdatedAt(java.time.LocalDateTime.now());
                    shipment.setOriginCountry(vendorShipment.getOriginCountry());
                    shipment.setDestCountry(vendorShipment.getDestCountry());
                    shipment.setAdmin(admin);

                    em.persist(shipment);

                    for (ShipProduct shipProduct : shipProducts) {
                        ShipmentItem shipmentItem = new ShipmentItem();
                        shipmentItem.setShipment(shipment);
                        shipmentItem.setShipProduct(shipProduct);
                        shipmentItem.setQuantity(shipProduct.getQuantity());
                        em.persist(shipmentItem);
                    }

                    ShipmentProgress shipmentProgress = new ShipmentProgress();
                    shipmentProgress.setShipStatus(shipStatus);
                    shipmentProgress.setLocation("From : " + vendorShipment.getOriginCountry().getName() + " To : " + vendorShipment.getDestCountry().getName());
                    shipmentProgress.setDescription("Shipment Accepted By Global Trade Logistics Corporation");
                    shipmentProgress.setCreatedAt(java.time.LocalDateTime.now());
                    shipmentProgress.setShipment(shipment);
                    em.persist(shipmentProgress);

                } else {
                    Warehouse warehouse = em.createNamedQuery("Warehouse.findById", Warehouse.class)
                            .setParameter("id", vendorShipment.getWarehouse().getId())
                            .getSingleResult();

                    List<Inventory> inventoryList = em.createNamedQuery("Inventory.findAll", Inventory.class).getResultList();

                    for (ShipProduct shipProduct : shipProducts) {
                        if (inventoryList.isEmpty()) {
                            System.out.println("New Inventory");
                            Inventory newInventory = new Inventory();
                            newInventory.setProductName(shipProduct.getName());
                            newInventory.setHsCode(shipProduct.getHsCode());
                            newInventory.setQuantity(shipProduct.getQuantity());
                            newInventory.setUnitValue(shipProduct.getUnitValue());
                            newInventory.setWarehouse(warehouse);
                            em.persist(newInventory);
                        } else {
                            for (Inventory inventory : inventoryList) {
                                if (inventory.getHsCode().equals(shipProduct.getHsCode()) && Objects.equals(inventory.getWarehouse().getId(), warehouse.getId())) {
                                    System.out.println("Inventory Update");
                                    inventory.setQuantity(inventory.getQuantity() + shipProduct.getQuantity());
                                    em.merge(inventory);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            return "Shipment Updated";

        } else {
            return "Shipment is not in Pending Status";
        }
    }

    public String updateProgress(String shipment_id, String status, String location, String description) {
        ShipStatus shipStatus = em.createNamedQuery("ShipStatus.findByName", ShipStatus.class)
                .setParameter("name", status)
                .getSingleResult();

        Shipment shipment = em.createNamedQuery("Shipment.findByShipmentId", Shipment.class).setParameter("shipmentIdString", shipment_id).getSingleResult();

        ShipmentProgress shipmentProgress = new ShipmentProgress();
        shipmentProgress.setShipStatus(shipStatus);
        shipmentProgress.setLocation(location);
        shipmentProgress.setDescription(description);
        shipmentProgress.setCreatedAt(java.time.LocalDateTime.now());
        shipmentProgress.setShipment(shipment);

        em.persist(shipmentProgress);

        return gson.toJson(Map.of(
                "status", true,
                "message", "Shipment Progress Updated"
        ));
    }

    public String saveShipment(String username, Map<String, Object> body) {
        Admin admin = em.createNamedQuery("Admin.findByUsername", Admin.class)
                .setParameter("username", username)
                .getSingleResult();

        String carrier = (String) body.get("carrier");
        String category = (String) body.get("category");
        String description = (String) body.get("description");
        String destAddress = (String) body.get("destAddress");
        String destCountry = (String) body.get("destCountry");
        String expectedDate = (String) body.get("expect_date");
        String originCountryId = (String) body.get("originCountryId");
        String originWarehouseId = (String) body.get("originWarehouseId");
        String recipientName = (String) body.get("recipientName");
        String recipientPhone = (String) body.get("recipientPhone");
        String shipment_id = (String) body.get("shipment_id");
        String status = (String) body.get("status");
        String weight = (String) body.get("weight");

        ArrayList<Map<String, Object>> productMaps = (ArrayList<Map<String, Object>>) body.get("products");

        Country originCountry = em.createNamedQuery("Country.findById", Country.class)
                .setParameter("id", Integer.parseInt(originCountryId))
                .getSingleResult();

        Country destinationCountry = em.createNamedQuery("Country.findByName", Country.class)
                .setParameter("name", destCountry)
                .getSingleResult();

        Warehouse originWarehouse = em.createNamedQuery("Warehouse.findById", Warehouse.class)
                .setParameter("id", Integer.parseInt(originWarehouseId))
                .getSingleResult();

        ShipStatus shipStatus = em.createNamedQuery("ShipStatus.findByName", ShipStatus.class)
                .setParameter("name", status)
                .getSingleResult();

        Shipment shipment = new Shipment();
        shipment.setShipmentIdString(shipment_id);
        shipment.setCarrier(carrier);
        shipment.setExpectData(LocalDate.parse(expectedDate).atStartOfDay());
        shipment.setWeight(Double.parseDouble(weight));
        shipment.setDescription(description);
        shipment.setOriginAddress(originCountry.getName() + " - " + originWarehouse.getName());
        shipment.setSenderName("Global Trade Logistics Corporation");
        shipment.setSenderPhone("0123456789");
        shipment.setDestAddress(destAddress);
        shipment.setRecipientName(recipientName);
        shipment.setRecipientPhone(recipientPhone);
        shipment.setCreatedAt(java.time.LocalDateTime.now());
        shipment.setUpdatedAt(java.time.LocalDateTime.now());
        shipment.setOriginCountry(originCountry);
        shipment.setDestCountry(destinationCountry);
        shipment.setAdmin(admin);
        em.persist(shipment);

        for (Map<String, Object> productMap : productMaps) {
            String id = (String) productMap.get("id");
            Object shipQuantityObj = productMap.get("shipQuantity");
            int shipQuantity;

            if (shipQuantityObj instanceof Number) {
                shipQuantity = ((Number) shipQuantityObj).intValue();
            } else if (shipQuantityObj instanceof String) {
                shipQuantity = Integer.parseInt((String) shipQuantityObj);
            } else {
                return gson.toJson(Map.of(
                        "status", false,
                        "message", "Invalid shipment quantity format"
                ));
            }

            Inventory inventory = em.createNamedQuery("Inventory.findById", Inventory.class)
                    .setParameter("id", Integer.parseInt(id))
                    .getSingleResult();

            if (inventory.getQuantity() >= shipQuantity) {
                inventory.setQuantity(inventory.getQuantity() - shipQuantity);
                em.merge(inventory);

                ShipmentItem shipmentItem = new ShipmentItem();
                shipmentItem.setInventory(inventory);
                shipmentItem.setQuantity(shipQuantity);
                shipmentItem.setShipment(shipment);
                em.persist(shipmentItem);

            } else {
                return gson.toJson(Map.of(
                        "status", false,
                        "message", "Not enough " + inventory.getProductName() + " in stock"
                ));
            }
        }

        ShipmentProgress shipmentProgress = new ShipmentProgress();
        shipmentProgress.setShipStatus(shipStatus);
        shipmentProgress.setLocation(originWarehouse.getName() + " - " + originCountry.getName());
        shipmentProgress.setDescription("Shipment Accepted By Global Trade Logistics Corporation");
        shipmentProgress.setCreatedAt(java.time.LocalDateTime.now());
        shipmentProgress.setShipment(shipment);
        em.persist(shipmentProgress);

        HashMap<String, Object> shipmentDetails = new HashMap<>();
        shipmentDetails.put("shipment_id", shipment.getShipmentIdString());
        shipmentDetails.put("carrier", shipment.getCarrier());
        shipmentDetails.put("expect_date", shipment.getExpectData().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy", java.util.Locale.ENGLISH)));
        shipmentDetails.put("weight", shipment.getWeight() + " kg");
        shipmentDetails.put("description", shipment.getDescription());
        shipmentDetails.put("originCountry", shipment.getOriginCountry().getName());
        shipmentDetails.put("originAddress", shipment.getOriginAddress());
        shipmentDetails.put("destCountry", shipment.getDestCountry().getName());
        shipmentDetails.put("destAddress", shipment.getDestAddress());
        shipmentDetails.put("category", "");
        shipmentDetails.put("status", shipStatus.getName());
        shipmentDetails.put("products", new ArrayList<>());

        return gson.toJson(Map.of(
                "status", true,
                "message", "Shipment Saved",
                "newShip", shipmentDetails
        ));
    }
}
