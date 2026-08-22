package me.dinuka.gtlc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipment_progress")
@NamedQueries({
        @NamedQuery(name = "ShipmentProgress.findAll", query = "SELECT s FROM ShipmentProgress s")
        , @NamedQuery(name = "ShipmentProgress.findByShipment", query = "SELECT s FROM ShipmentProgress s WHERE s.shipment = :shipment")
})
public class ShipmentProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "location", length = 45)
    private String location;

    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ship_status_id", nullable = false)
    private ShipStatus shipStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    public ShipmentProgress() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public ShipStatus getShipStatus() { return shipStatus; }
    public void setShipStatus(ShipStatus shipStatus) { this.shipStatus = shipStatus; }

    public Shipment getShipment() { return shipment; }
    public void setShipment(Shipment shipment) { this.shipment = shipment; }
}

