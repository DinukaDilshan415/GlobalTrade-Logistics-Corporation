package me.dinuka.gtlc.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "custom_status")
@NamedQueries({
        @NamedQuery(name = "CustomStatus.findAll", query = "SELECT c FROM CustomStatus c")
        , @NamedQuery(name = "CustomStatus.findByStatus", query = "SELECT c FROM CustomStatus c WHERE c.status = :status")
        , @NamedQuery(name = "CustomStatus.findByStatusId", query = "SELECT c FROM CustomStatus c WHERE c.id = :id")
})
public class CustomStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "status", length = 45, nullable = false)
    private String status;

    public CustomStatus() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

