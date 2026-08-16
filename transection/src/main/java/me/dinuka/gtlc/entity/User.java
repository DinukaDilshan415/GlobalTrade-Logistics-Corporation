package me.dinuka.gtlc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
@NamedQueries({
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
        , @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
        , @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", length = 45, nullable = false)
    private String username;

    @Column(name = "email", length = 45, nullable = false)
    private String email;

    @Lob // Specifies a large object type for the TEXT definition
    @Column(name = "password_hash", columnDefinition = "TEXT", nullable = false)
    private String passwordHash;

    // Many-to-One structural mapping
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_type_id", nullable = false)
    private AccountType accountType;

    // Constructors
    public User() {}

    public User(String username, String email, String passwordHash, AccountType accountType) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.accountType = accountType;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
}
