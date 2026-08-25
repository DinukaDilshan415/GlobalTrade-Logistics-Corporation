package me.dinuka.gtlc.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "document_type")
@NamedQueries({
        @NamedQuery(name = "DocumentType.findAll", query = "SELECT d FROM DocumentType d")
        , @NamedQuery(name = "DocumentType.findByType", query = "SELECT d FROM DocumentType d WHERE d.type = :type")
        , @NamedQuery(name = "DocumentType.findByTypeId", query = "SELECT d FROM DocumentType d WHERE d.id = :id")
})
public class DocumentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type", length = 45, nullable = false)
    private String type;

    public DocumentType() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}

