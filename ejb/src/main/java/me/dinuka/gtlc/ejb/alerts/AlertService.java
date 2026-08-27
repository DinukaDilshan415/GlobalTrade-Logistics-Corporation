package me.dinuka.gtlc.ejb.alerts;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.gtlc.entity.Alert;
import me.dinuka.gtlc.enums.AlertSeverity;
import me.dinuka.gtlc.enums.AlertStatus;
import me.dinuka.gtlc.enums.AlertType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Stateless
public class AlertService {
    @PersistenceContext(unitName = "gtlcPU")
    private EntityManager em;

    public Alert createAlert(
            AlertType type,
            AlertSeverity severity,
            String message,
            String entityType,
            String entityId) {

        List<Alert> alertList = em.createQuery("SELECT a FROM Alert a WHERE a.type = :type AND a.entityId = :entityId", Alert.class)
                .setParameter("type", type.name())
                .setParameter("entityId", entityId)
                .getResultList();

        if(alertList.isEmpty()){
            Alert alert = new Alert();

            int number = ThreadLocalRandom.current().nextInt(1000000);
            String result = String.format("ALT-%06d", number);

            alert.setAlertNumber(result);
            alert.setType(type.name());
            alert.setSeverity(severity.name());
            alert.setMessage(message);
            alert.setEntityType(entityType);
            alert.setEntityId(entityId);
            alert.setStatus(AlertStatus.UNREAD.name());
            alert.setCreatedAt(LocalDateTime.now());
            alert.setReadAt(LocalDateTime.now());

            em.persist(alert);

            System.out.println("Alert created: " + alert.getAlertNumber() + " | "+ alert.getMessage()+" | "+alert.getEntityId());

            return alert;
        } else {
            System.out.println("Alert already exists: " + alertList.get(0).getAlertNumber());
            return alertList.get(0);
        }
    }
}
