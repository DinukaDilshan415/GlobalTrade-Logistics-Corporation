package me.dinuka.gtlc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@NamedQueries({
        @NamedQuery(name = "Alert.findAll", query = "SELECT a FROM Alert a")
        , @NamedQuery(name = "Alert.findByAlertNumber", query = "SELECT a FROM Alert a WHERE a.alertNumber = :alertNumber")
        , @NamedQuery(name = "Alert.findByEntityType", query = "SELECT a FROM Alert a WHERE a.entityType = :entityType")
        , @NamedQuery(name = "Alert.findByEntityId", query = "SELECT a FROM Alert a WHERE a.entityId = :entityId")
        , @NamedQuery(name = "Alert.findByStatus", query = "SELECT a FROM Alert a WHERE a.status = :status")
})
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "alert_number", length = 45)
    private String alertNumber;

    @Column(name = "type", length = 45)
    private String type;

    @Column(name = "severity", length = 45)
    private String severity;

    @Lob
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "entity_type", length = 45)
    private String entityType;

    @Column(name = "entity_id", length = 45)
    private String entityId;

    @Column(name = "status", length = 45)
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    public Alert() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getAlertNumber() { return alertNumber; }
    public void setAlertNumber(String alertNumber) { this.alertNumber = alertNumber; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }

    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }
}

