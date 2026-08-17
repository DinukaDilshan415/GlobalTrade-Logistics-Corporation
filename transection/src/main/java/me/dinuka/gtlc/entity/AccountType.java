package me.dinuka.gtlc.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "account_type")
@NamedQueries({
        @NamedQuery(name = "AccountType.findAll", query = "SELECT a FROM AccountType a")
        , @NamedQuery(name = "AccountType.findByType", query = "SELECT a FROM AccountType a WHERE a.type = :type")
})
public class AccountType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type", length = 45, nullable = false)
    private String type;

    @OneToMany(mappedBy = "accountType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users;

    public AccountType() {}

    public AccountType(String type) {
        this.type = type;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }
}
