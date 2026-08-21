package me.dinuka.gtlc.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ship_status")
@NamedQueries({
        @NamedQuery(name = "ShipStatus.findAll", query = "SELECT s FROM ShipStatus s")
        , @NamedQuery(name = "ShipStatus.findByName", query = "SELECT s FROM ShipStatus s WHERE s.name = :name")
        , @NamedQuery(name = "ShipStatus.findById", query = "SELECT s FROM ShipStatus s WHERE s.id = :id")
})
public class ShipStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 45, nullable = false)
    private String name;

    public ShipStatus() {}

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

