package me.dinuka.gtlc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "shipment_items")
@NamedQueries({
        @NamedQuery(name = "ShipmentItem.findAll", query = "SELECT s FROM ShipmentItem s")
        , @NamedQuery(name = "ShipmentItem.findByShipment", query = "SELECT s FROM ShipmentItem s WHERE s.shipment = :shipment")
})
public class ShipmentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ship_product_id", nullable = false)
    private ShipProduct shipProduct;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    public ShipmentItem() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ShipProduct getShipProduct() { return shipProduct; }
    public void setShipProduct(ShipProduct shipProduct) { this.shipProduct = shipProduct; }

    public Inventory getInventory() { return inventory; }
    public void setInventory(Inventory inventory) { this.inventory = inventory; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Shipment getShipment() { return shipment; }
    public void setShipment(Shipment shipment) { this.shipment = shipment; }
}

