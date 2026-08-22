package me.dinuka.gtlc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vendor_shipment")
@NamedQueries({
        @NamedQuery(name = "VendorShipment.findAll", query = "SELECT v FROM VendorShipment v")
        , @NamedQuery(name = "VendorShipment.findByShipmentId", query = "SELECT v FROM VendorShipment v WHERE v.shipmentIdString = :shipmentIdString")
        , @NamedQuery(name = "VendorShipment.findByShipmentIdString", query = "SELECT v FROM VendorShipment v WHERE v.shipmentIdString = :shipmentIdString")
        , @NamedQuery(name = "VendorShipment.findByVendor", query = "SELECT v FROM VendorShipment v WHERE v.vendor= :vendor"),
        @NamedQuery(
                name = "VendorShipment.findAllByStatusId",
                query = "SELECT vs FROM VendorShipment vs WHERE vs.shipStatus.id = :statusId"
        )
})
public class VendorShipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "shipment_id", length = 45, nullable = false)
    private String shipmentIdString; // Avoids keyword collision with primary key 'id'

    @Column(name = "carrier", length = 45)
    private String carrier;

    @Column(name = "expect_data") // Notice layout says 'expect_data' DATETIME
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouses_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "origin_country_id", nullable = false)
    private Country originCountry;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dest_country_id", nullable = false)
    private Country destCountry;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ship_category_id", nullable = false)
    private ShipCategory shipCategory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ship_status_id", nullable = false)
    private ShipStatus shipStatus;

    @OneToMany(mappedBy = "vendorShipment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ShipProduct> products;

    public VendorShipment() {}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(String originAddress) {
        this.originAddress = originAddress;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Country getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(Country originCountry) {
        this.originCountry = originCountry;
    }

    public Country getDestCountry() {
        return destCountry;
    }

    public void setDestCountry(Country destCountry) {
        this.destCountry = destCountry;
    }

    public ShipCategory getShipCategory() {
        return shipCategory;
    }

    public void setShipCategory(ShipCategory shipCategory) {
        this.shipCategory = shipCategory;
    }

    public ShipStatus getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(ShipStatus shipStatus) {
        this.shipStatus = shipStatus;
    }

    public List<ShipProduct> getProducts() {
        return products;
    }

    public void setProducts(List<ShipProduct> products) {
        this.products = products;
    }
}

