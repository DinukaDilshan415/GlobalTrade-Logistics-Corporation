package me.dinuka.gtlc.ejb.alerts;

import com.google.gson.Gson;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.gtlc.entity.Alert;
import me.dinuka.gtlc.enums.AlertSeverity;
import me.dinuka.gtlc.enums.AlertStatus;
import me.dinuka.gtlc.enums.AlertType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Stateless
public class AlertService {
    @PersistenceContext(unitName = "gtlcPU")
    private EntityManager em;

    Gson gson = new Gson();

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

        if (alertList.isEmpty()) {
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

            System.out.println("Alert created: " + alert.getAlertNumber() + " | " + alert.getMessage() + " | " + alert.getEntityId());

            return alert;
        } else {
            System.out.println("Alert already exists: " + alertList.get(0).getAlertNumber());
            return alertList.get(0);
        }
    }

    public String getAllAlerts() {
        List<Alert> alertList = em.createNamedQuery("Alert.findAll", Alert.class).getResultList();

        ArrayList<Map<String, String>> alerts = new ArrayList<>();
        for (Alert alert : alertList) {
            HashMap<String, String> alertMap = new HashMap<>();
            alertMap.put("id", alert.getAlertNumber());
            alertMap.put("type", alert.getType());
            alertMap.put("severity", alert.getSeverity());
            alertMap.put("message", alert.getMessage());
            alertMap.put("relatedEntity", alert.getEntityId());
            alertMap.put("status", alert.getStatus());
            alertMap.put("datetime", alert.getCreatedAt().format(java.time.format.DateTimeFormatter
                    .ofPattern("MMM dd, yyyy HH:mm:ss", java.util.Locale.ENGLISH)));
            alerts.add(alertMap);
        }

        return gson.toJson(Map.of(
                "status", true,
                "data", alerts
        ));
    }

    public String updateAlertStatus(String alertNumber, String status) {

        if (status.equals("ACKNOWLEDGED")) {
            Alert alert = em.createNamedQuery("Alert.findByAlertNumber", Alert.class)
                    .setParameter("alertNumber", alertNumber)
                    .getSingleResult();

            alert.setStatus(AlertStatus.ACKNOWLEDGED.name());
            alert.setReadAt(LocalDateTime.now());
            em.merge(alert);

            return gson.toJson(Map.of(
                    "status", true,
                    "message", "Alert status updated successfully"
            ));
        } else {
            return gson.toJson(Map.of(
                    "status", false,
                    "message", "Invalid status"
            ));
        }
    }
}


