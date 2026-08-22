package me.dinuka.gtlc.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "inventory")
@NamedQueries({
        @NamedQuery(name = "Inventory.findAll", query = "SELECT i FROM Inventory i")
        , @NamedQuery(name = "Inventory.findById", query = "SELECT i FROM Inventory i WHERE i.id = :id")
        , @NamedQuery(name = "Inventory.findByWarehouse", query = "SELECT i FROM Inventory i WHERE i.warehouse = :warehouse")
        , @NamedQuery(name = "Inventory.findByWarehouseWithValidQty", query = "SELECT i FROM Inventory i WHERE i.warehouse = :warehouse AND i.quantity > 0")
})
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_name", length = 45, nullable = false)
    private String productName;

    @Column(name = "hs_code", length = 45)
    private String hsCode;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_value")
    private Integer unitValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouses_id", nullable = false)
    private Warehouse warehouse;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ShipmentItem> shipmentItems;

    public Inventory() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getHsCode() { return hsCode; }
    public void setHsCode(String hsCode) { this.hsCode = hsCode; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getUnitValue() { return unitValue; }
    public void setUnitValue(Integer unitValue) { this.unitValue = unitValue; }

    public Warehouse getWarehouse() { return warehouse; }
    public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }

    public List<ShipmentItem> getShipmentItems() { return shipmentItems; }
    public void setShipmentItems(List<ShipmentItem> shipmentItems) { this.shipmentItems = shipmentItems; }
}

