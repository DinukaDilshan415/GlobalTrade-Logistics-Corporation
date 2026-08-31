package me.dinuka.gtlc.ejb;

import com.google.gson.Gson;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.gtlc.annotation.Logged;
import me.dinuka.gtlc.dto.InventoryDTO;
import me.dinuka.gtlc.entity.Inventory;
import me.dinuka.gtlc.entity.Warehouse;
import me.dinuka.gtlc.log.ApplicationLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

@Logged
@Stateless
public class InventorySessionBean {
    @PersistenceContext(unitName = "gtlcPU")
    private EntityManager em;

    private static final Logger LOGGER = ApplicationLogger.getLogger();

    Gson gson = new Gson();

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public String getAllInventory(){
        List<Inventory> inventoryList = em.createNamedQuery("Inventory.findAll", Inventory.class).getResultList();

        if(inventoryList.isEmpty()){
            return gson.toJson(Map.of(
                    "status", false,
                    "message", "No Inventory Found"));
        } else {
            ArrayList<Map<String, Object>> inventorArrayList = new ArrayList<>();

            for(Inventory inventory : inventoryList){
                Map<String, Object> inventoryMap = Map.of(
                        "id", inventory.getId(),
                        "product_name", inventory.getProductName(),
                        "hs_code", inventory.getHsCode(),
                        "quantity", inventory.getQuantity(),
                        "unit_value", inventory.getUnitValue(),
                        "warehouses_id", inventory.getWarehouse().getId()
                );
                inventorArrayList.add(inventoryMap);
            }

            return gson.toJson(Map.of(
                    "status", true,
                    "data", inventorArrayList
            ));
        }

    }

    public String addNewInventory(InventoryDTO dto){
        String product_name = dto.getProduct_name();
        String hs_code = dto.getHs_code();
        String quantity = dto.getQuantity();
        String unit_value = dto.getUnit_value();
        String warehouses_id = dto.getWarehouses_id();

        Warehouse warehouse = em.createNamedQuery("Warehouse.findById", Warehouse.class)
                .setParameter("id", Integer.valueOf(warehouses_id))
                .getSingleResult();

        List<Inventory> inventoryList = em.createNamedQuery("Inventory.findAll", Inventory.class).getResultList();

        boolean foundMatch = false;

        if (!inventoryList.isEmpty()) {
            for (Inventory inventory : inventoryList) {
                if (inventory.getHsCode().equals(hs_code) && Objects.equals(inventory.getWarehouse().getId(), warehouse.getId())) {
                    System.out.println("Inventory Update: Updated quantity for HS Code " + hs_code);
                    inventory.setQuantity(inventory.getQuantity() + Integer.parseInt(quantity));
                    em.merge(inventory);
                    foundMatch = true;
                    break;
                }
            }
        }

        if (!foundMatch) {
            System.out.println("New Inventory : Creating new inventory for HS Code " + hs_code);
            Inventory newInventory = new Inventory();
            newInventory.setProductName(product_name);
            newInventory.setHsCode(hs_code);
            newInventory.setQuantity(Integer.valueOf(quantity));
            newInventory.setUnitValue(Integer.valueOf(unit_value));
            newInventory.setWarehouse(warehouse);
            em.persist(newInventory);
        }

        LOGGER.info("Inventory Added Successfully"+" | HS Code: "+hs_code+" | Quantity: "+quantity+" | Warehouse ID: "+warehouses_id);
        return gson.toJson(Map.of(
                "status", true,
                "message", "Inventory Added Successfully"
        ));
    }
}
