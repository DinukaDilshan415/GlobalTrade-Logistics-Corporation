package me.dinuka.gtlc.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_status")
@NamedQueries({
        @NamedQuery(name = "UserStatus.findAll", query = "SELECT u FROM UserStatus u")
        , @NamedQuery(name = "UserStatus.findByStatus", query = "SELECT u FROM UserStatus u WHERE u.status = :status")
        , @NamedQuery(name = "UserStatus.findByStatusId", query = "SELECT u FROM UserStatus u WHERE u.id = :id")
})
public class UserStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "status", length = 45, nullable = false)
    private String status;

    public UserStatus() {}

    public UserStatus(String status) {
        this.status = status;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

