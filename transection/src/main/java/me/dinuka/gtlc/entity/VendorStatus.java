package me.dinuka.gtlc.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "vendor_status")
@NamedQueries({
        @NamedQuery(name = "VendorStatus.findAll", query = "SELECT v FROM VendorStatus v")
        , @NamedQuery(name = "VendorStatus.findByStatus", query = "SELECT v FROM VendorStatus v WHERE v.status = :status")
        , @NamedQuery(name = "VendorStatus.findByStatusId", query = "SELECT v FROM VendorStatus v WHERE v.id = :id")
})
public class VendorStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "status", length = 45, nullable = false)
    private String status;

    @OneToMany(mappedBy = "vendorStatus", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Vendor> vendors;

    public VendorStatus() {}

    public VendorStatus(String status) {
        this.status = status;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<Vendor> getVendors() { return vendors; }
    public void setVendors(List<Vendor> vendors) { this.vendors = vendors; }
}

