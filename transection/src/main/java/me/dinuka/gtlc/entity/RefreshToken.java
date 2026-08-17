package me.dinuka.gtlc.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_token",
        indexes = {
            @Index(columnList = "token", unique = true),
            @Index(columnList = "email", unique = true),
        })
@NamedQueries({
        @NamedQuery(name = "RefreshToken.findByValidToken",
                query = "SELECT rf FROM RefreshToken rf WHERE rf.token=:token AND rf.expiryAt > :now"),
        @NamedQuery(name = "RefreshToken.deleteToken",
                query = "DELETE FROM RefreshToken rf WHERE rf.token=:token"),
        @NamedQuery(name = "RefreshToken.deleteExpiredToken",
                query = "DELETE FROM RefreshToken rf WHERE rf.expiryAt < :now"),
        @NamedQuery(
                name = "RefreshToken.deleteByUsername",
                query = "DELETE FROM RefreshToken WHERE email = :username"
        ),
        @NamedQuery(name = "RefreshToken.findByEmail", query = "SELECT rf FROM RefreshToken rf WHERE rf.email = :email")

})
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String token;
    private String email;
    private Instant expiryAt;
    private Instant createdAt = Instant.now();

    public RefreshToken() {
    }

    public RefreshToken(String token, String email, Instant expiryAt) {
        this.token = token;
        this.email = email;
        this.expiryAt = expiryAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public Instant getExpiryAt() {
        return expiryAt;
    }

    public void setExpiryAt(Instant expiryAt) {
        this.expiryAt = expiryAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
