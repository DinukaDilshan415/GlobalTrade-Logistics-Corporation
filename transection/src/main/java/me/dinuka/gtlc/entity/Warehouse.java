package me.dinuka.gtlc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "warehouses")
@NamedQueries({
        @NamedQuery(name = "Warehouse.findAll", query = "SELECT w FROM Warehouse w")
        , @NamedQuery(name = "Warehouse.findByName", query = "SELECT w FROM Warehouse w WHERE w.name = :name")
        , @NamedQuery(name = "Warehouse.findById", query = "SELECT w FROM Warehouse w WHERE w.id = :id")
        , @NamedQuery(name = "Warehouse.findByCountry", query = "SELECT w FROM Warehouse w WHERE w.country = :country"),
        @NamedQuery(
                name = "Warehouse.findDistinctCountries",
                query = "SELECT DISTINCT w.country FROM Warehouse w"
        )
})
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 45, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    public Warehouse() {
    }

    public Warehouse(String name, Country country) {
        this.name = name;
        this.country = country;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}

