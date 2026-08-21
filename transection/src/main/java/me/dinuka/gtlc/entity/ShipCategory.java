package me.dinuka.gtlc.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ship_category")
@NamedQueries({
        @NamedQuery(name = "ShipCategory.findAll", query = "SELECT c FROM ShipCategory c")
        , @NamedQuery(name = "ShipCategory.findByName", query = "SELECT c FROM ShipCategory c WHERE c.name = :name")
        , @NamedQuery(name = "ShipCategory.findById", query = "SELECT c FROM ShipCategory c WHERE c.id = :id")
})
public class ShipCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 45, nullable = false)
    private String name;

    public ShipCategory() {}

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

