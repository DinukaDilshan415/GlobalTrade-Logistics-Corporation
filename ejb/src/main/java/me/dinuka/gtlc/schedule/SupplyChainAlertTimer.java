package me.dinuka.gtlc.schedule;

import jakarta.ejb.EJB;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import me.dinuka.gtlc.annotation.MonitorTimeout;
import me.dinuka.gtlc.ejb.alerts.CustomsService;
import me.dinuka.gtlc.ejb.alerts.InventoryService;
import me.dinuka.gtlc.ejb.alerts.ShipmentService;

@Singleton
@Startup
public class SupplyChainAlertTimer {

    @EJB
    private ShipmentService shipmentService;

    @EJB
    private InventoryService inventoryService;

    @EJB
    private CustomsService customsService;

    @MonitorTimeout
    @Schedule(
            hour = "*",
            minute = "*/30",
            second = "0",
            persistent = true
    )
    public void monitorSupplyChain() {

        shipmentService.checkDelayedShipments();

        inventoryService.checkLowStock();

        customsService.checkCustomsDeadlines();
    }
}
