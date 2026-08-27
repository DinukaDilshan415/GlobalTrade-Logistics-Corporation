package me.dinuka.gtlc.ejb.alerts;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.gtlc.entity.Inventory;
import me.dinuka.gtlc.enums.AlertSeverity;
import me.dinuka.gtlc.enums.AlertType;

import java.util.List;

@Stateless
public class InventoryService {
    @PersistenceContext(unitName = "gtlcPU")
    private EntityManager em;

    @EJB
    private AlertService alertService;

    public void checkLowStock(){
        int minimumStock = 20;

        List<Inventory> inventoryList = em.createNamedQuery("Inventory.findAll", Inventory.class).getResultList();

        for(Inventory inventory : inventoryList){
            if(inventory.getQuantity() < minimumStock){
                checkInventory(inventory);
            }
        }
    }

    private void checkInventory(Inventory inventory) {
        alertService.createAlert(
                AlertType.INVENTORY_SHORTAGE,
                AlertSeverity.CRITICAL,
                inventory.getHsCode()+" - "+inventory.getProductName()+ " is below the minimum stock level in "+inventory.getWarehouse().getName(),
                "INVENTORY",
                inventory.getId().toString()
        );
    }
}
