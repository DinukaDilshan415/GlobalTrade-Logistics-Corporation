package me.dinuka.gtlc.ejb.alerts;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.gtlc.entity.Shipment;
import me.dinuka.gtlc.entity.ShipmentProgress;
import me.dinuka.gtlc.enums.AlertSeverity;
import me.dinuka.gtlc.enums.AlertType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class ShipmentService {

    @PersistenceContext(unitName = "gtlcPU")
    private EntityManager em;

    @EJB
    private AlertService alertService;
    
    public void checkDelayedShipments(){
        List<Shipment> shipmentList = em.createNamedQuery("Shipment.findAll", Shipment.class).getResultList();
        
        for(Shipment shipment : shipmentList){
            System.out.println(shipment.getShipmentIdString() + " | " + shipment.getExpectData());

            ShipmentProgress shipmentProgress = em.createNamedQuery("ShipmentProgress.findByShipmentDesc", ShipmentProgress.class)
                    .setParameter("shipment", shipment)
                    .setMaxResults(1).getSingleResult();

            if (!shipmentProgress.getShipStatus().getName().equals("DELIVERED")) {
                if (shipment.getExpectData().isBefore(LocalDateTime.now())) {
                    checkShipment(shipment);
                }
            }
        }
    }

    public void checkShipment(Shipment shipment) {
            alertService.createAlert(
                    AlertType.SHIPMENT_DELAY,
                    AlertSeverity.HIGH,
                    "Shipment "+shipment.getShipmentIdString()+" is Delayed",
                    "SHIPMENT",
                    shipment.getShipmentIdString()
            );
    }
}
