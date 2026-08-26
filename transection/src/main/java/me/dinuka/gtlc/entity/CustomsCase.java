package me.dinuka.gtlc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customs_cases")
@NamedQueries({
        @NamedQuery(name = "CustomsCase.findAll", query = "SELECT c FROM CustomsCase c")
        , @NamedQuery(name = "CustomsCase.findAllInOrder", query = "SELECT c FROM CustomsCase c ORDER BY c.submittedAt DESC ")
        , @NamedQuery(name = "CustomsCase.findById", query = "SELECT c FROM CustomsCase c WHERE c.id = :id")
        , @NamedQuery(name = "CustomsCase.findByCaseNumber", query = "SELECT c FROM CustomsCase c WHERE c.caseNumber = :caseNumber")
        , @NamedQuery(name = "CustomsCase.findByShipment", query = "SELECT c FROM CustomsCase c WHERE c.shipment = :shipment")
        , @NamedQuery(name = "CustomsCase.findByShipments", query = "SELECT c FROM CustomsCase c WHERE c.shipment IN :shipments")

})
public class CustomsCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "case_number", length = 45, nullable = false)
    private String caseNumber;

    @Column(name = "risk_level", length = 45)
    private String riskLevel;

    @Column(name = "customs_value")
    private Double customsValue;

    @Column(name = "estimated_duty")
    private Double estimatedDuty;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "response_at")
    private LocalDateTime responseAt;

    @Lob
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "custom_agent_id")
    private CustomAgent customAgent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "custom_status_id", nullable = false)
    private CustomStatus customStatus;

    @OneToMany(mappedBy = "customsCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CustomsDocument> customsDocuments;

    public CustomsCase() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCaseNumber() { return caseNumber; }
    public void setCaseNumber(String caseNumber) { this.caseNumber = caseNumber; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public Double getCustomsValue() { return customsValue; }
    public void setCustomsValue(Double customsValue) { this.customsValue = customsValue; }

    public Double getEstimatedDuty() { return estimatedDuty; }
    public void setEstimatedDuty(Double estimatedDuty) { this.estimatedDuty = estimatedDuty; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getResponseAt() { return responseAt; }
    public void setResponseAt(LocalDateTime responseAt) { this.responseAt = responseAt; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public Shipment getShipment() { return shipment; }
    public void setShipment(Shipment shipment) { this.shipment = shipment; }

    public CustomAgent getCustomAgent() { return customAgent; }
    public void setCustomAgent(CustomAgent customAgent) { this.customAgent = customAgent; }

    public CustomStatus getCustomStatus() { return customStatus; }
    public void setCustomStatus(CustomStatus customStatus) { this.customStatus = customStatus; }

    public List<CustomsDocument> getCustomsDocuments() { return customsDocuments; }
    public void setCustomsDocuments(List<CustomsDocument> customsDocuments) { this.customsDocuments = customsDocuments; }
}

