package me.dinuka.gtlc.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "country")
@NamedQueries({
        @NamedQuery(name = "Country.findAll", query = "SELECT c FROM Country c")
        , @NamedQuery(name = "Country.findByName", query = "SELECT c FROM Country c WHERE c.name = :name")
        , @NamedQuery(name = "Country.findById", query = "SELECT c FROM Country c WHERE c.id = :id")
})
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 45, nullable = false)
    private String name;

    public Country() {}

    public Country(String name) {
        this.name = name;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

}

