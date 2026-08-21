package me.dinuka.gtlc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ship_product")
public class ShipProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 45, nullable = false)
    private String name;

    @Column(name = "hs_code", length = 45)
    private String hsCode;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_value")
    private Integer unitValue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendor_shipment_id", nullable = false)
    private VendorShipment vendorShipment;

    public ShipProduct() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getHsCode() { return hsCode; }
    public void setHsCode(String hsCode) { this.hsCode = hsCode; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getUnitValue() { return unitValue; }
    public void setUnitValue(Integer unitValue) { this.unitValue = unitValue; }

    public VendorShipment getVendorShipment() { return vendorShipment; }
    public void setVendorShipment(VendorShipment vendorShipment) { this.vendorShipment = vendorShipment; }
}

