package com.devops.mrp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recharge_transactions")
public class RechargeTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String txnReference;

    @Column(nullable = false)
    private String mobileNumber;

    @Column(nullable = false)
    private String operator;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String planName;

    @Column(nullable = false)
    private String status; // SUCCESS, PENDING, FAILED

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private String notes;

    public RechargeTransaction() {
        this.createdAt = LocalDateTime.now();
    }

    public RechargeTransaction(String txnReference, String mobileNumber, String operator, Double amount, String planName, String status, String notes) {
        this.txnReference = txnReference;
        this.mobileNumber = mobileNumber;
        this.operator = operator;
        this.amount = amount;
        this.planName = planName;
        this.status = status;
        this.notes = notes;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTxnReference() {
        return txnReference;
    }

    public void setTxnReference(String txnReference) {
        this.txnReference = txnReference;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
