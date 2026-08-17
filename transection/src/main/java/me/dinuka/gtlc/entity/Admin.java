package me.dinuka.gtlc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin")
@NamedQueries({
        @NamedQuery(name = "Admin.findAll", query = "SELECT a FROM Admin a")
        , @NamedQuery(name = "Admin.findByUsername", query = "SELECT a FROM Admin a WHERE a.username = :username")
})
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", length = 45, nullable = false)
    private String username;

    @Lob
    @Column(name = "password_hash", columnDefinition = "TEXT", nullable = false)
    private String passwordHash;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Many-to-One structural mapping linking to the roles table
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roles_id", nullable = false)
    private Roles roles;

    // Constructors
    public Admin() {}

    public Admin(String username, String passwordHash, LocalDateTime createdAt, Roles roles) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.roles = roles;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Roles getRoles() { return roles; }
    public void setRoles(Roles roles) { this.roles = roles; }
}

