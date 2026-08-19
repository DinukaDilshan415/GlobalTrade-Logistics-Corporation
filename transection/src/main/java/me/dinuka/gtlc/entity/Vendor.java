package me.dinuka.gtlc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendor")
@NamedQueries({
        @NamedQuery(name = "Vendor.findAll", query = "SELECT v FROM Vendor v"),
        @NamedQuery(name = "Vendor.findByUser", query = "SELECT v FROM Vendor v WHERE v.user = :user")
        , @NamedQuery(name = "Vendor.findByVendorId", query = "SELECT v FROM Vendor v WHERE v.vendorIdString = :vendorIdString")
})
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "vendor_id", length = 45, nullable = false)
    private String vendorIdString;

    @Column(name = "company_name", length = 45, nullable = false)
    private String companyName;

    @Column(name = "contact_person", length = 45)
    private String contactPerson;

    @Column(name = "email", length = 45)
    private String email;

    @Column(name = "phone", length = 45)
    private String phone;

    @Lob
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "reg_number", length = 45)
    private String regNumber;

    @Lob
    @Column(name = "compliance_information", columnDefinition = "TEXT")
    private String complianceInformation;

    @Column(name = "req_date")
    private LocalDateTime reqDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendor_status_id", nullable = false)
    private VendorStatus vendorStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Vendor() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getVendorIdString() { return vendorIdString; }
    public void setVendorIdString(String vendorIdString) { this.vendorIdString = vendorIdString; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getRegNumber() { return regNumber; }
    public void setRegNumber(String regNumber) { this.regNumber = regNumber; }

    public String getComplianceInformation() { return complianceInformation; }
    public void setComplianceInformation(String complianceInformation) { this.complianceInformation = complianceInformation; }

    public LocalDateTime getReqDate() { return reqDate; }
    public void setReqDate(LocalDateTime reqDate) { this.reqDate = reqDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Country getCountry() { return country; }
    public void setCountry(Country country) { this.country = country; }

    public VendorStatus getVendorStatus() { return vendorStatus; }
    public void setVendorStatus(VendorStatus vendorStatus) { this.vendorStatus = vendorStatus; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}


