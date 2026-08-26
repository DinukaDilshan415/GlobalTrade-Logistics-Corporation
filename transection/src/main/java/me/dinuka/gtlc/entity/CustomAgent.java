package me.dinuka.gtlc.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "custom_agent")
@NamedQueries({
        @NamedQuery(name = "CustomAgent.findAll", query = "SELECT c FROM CustomAgent c")
        , @NamedQuery(name = "CustomAgent.findById", query = "SELECT c FROM CustomAgent c WHERE c.id = :id")
        , @NamedQuery(name = "CustomAgent.findByCountry", query = "SELECT c FROM CustomAgent c WHERE c.country = :country")
        , @NamedQuery(name = "CustomAgent.findByUser", query = "SELECT c FROM CustomAgent c WHERE c.user = :user")
})
public class CustomAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 45, nullable = false)
    private String name;

    @Column(name = "position", length = 45)
    private String position;

    @Column(name = "reg_number", length = 45)
    private String regNumber;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_status_id", nullable = false)
    private UserStatus userStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "customAgent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CustomsCase> customsCases;

    public CustomAgent() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getRegNumber() { return regNumber; }
    public void setRegNumber(String regNumber) { this.regNumber = regNumber; }

    public List<CustomsCase> getCustomsCases() { return customsCases; }
    public void setCustomsCases(List<CustomsCase> customsCases) { this.customsCases = customsCases; }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

