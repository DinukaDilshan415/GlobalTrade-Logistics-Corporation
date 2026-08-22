package me.dinuka.gtlc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "shipment")
@NamedQueries({
        @NamedQuery(name = "Shipment.findAll", query = "SELECT s FROM Shipment s")
        , @NamedQuery(name = "Shipment.findByShipmentId", query = "SELECT s FROM Shipment s WHERE s.shipmentIdString = :shipmentIdString")
})
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "shipment_id", length = 45, nullable = false)
    private String shipmentIdString;

    @Column(name = "carrier", length = 45)
    private String carrier;

    @Column(name = "expect_data")
    private LocalDateTime expectData;

    @Column(name = "weight")
    private Double weight;

    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "origin_address", length = 100)
    private String originAddress;

    @Column(name = "sender_name", length = 45)
    private String senderName;

    @Column(name = "sender_phone", length = 45)
    private String senderPhone;

    @Column(name = "dest_address", length = 100)
    private String destAddress;

    @Column(name = "recipient_name", length = 45)
    private String recipientName;

    @Column(name = "recipient_phone", length = 45)
    private String recipientPhone;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "origin_country_id", nullable = false)
    private Country originCountry;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dest_country_id", nullable = false)
    private Country destCountry;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ShipmentItem> shipmentItems;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ShipmentProgress> progressLogs;

    public Shipment() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShipmentIdString() {
        return shipmentIdString;
    }

    public void setShipmentIdString(String shipmentIdString) {
        this.shipmentIdString = shipmentIdString;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public LocalDateTime getExpectData() {
        return expectData;
    }

    public void setExpectData(LocalDateTime expectData) {
        this.expectData = expectData;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public List<ShipmentProgress> getProgressLogs() {
        return progressLogs;
    }

    public void setProgressLogs(List<ShipmentProgress> progressLogs) {
        this.progressLogs = progressLogs;
    }

    public List<ShipmentItem> getShipmentItems() {
        return shipmentItems;
    }

    public void setShipmentItems(List<ShipmentItem> shipmentItems) {
        this.shipmentItems = shipmentItems;
    }

    public Country getDestCountry() {
        return destCountry;
    }

    public void setDestCountry(Country destCountry) {
        this.destCountry = destCountry;
    }

    public Country getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(Country originCountry) {
        this.originCountry = originCountry;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(String originAddress) {
        this.originAddress = originAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getters and Setters (Generate typical standard code wrappers here)
}

