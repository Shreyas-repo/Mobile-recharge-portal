package com.devops.mrp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "recharge_plans")
public class RechargePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String operator; // Airtel, Jio, Vi, BSNL

    @Column(nullable = false)
    private String planName;

    @Column(nullable = false)
    private String category; // Popular, Unlimited, Data, Talktime

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Integer validityDays;

    @Column(nullable = false)
    private String dataBenefit;

    @Column(nullable = false)
    private String description;

    public RechargePlan() {
    }

    public RechargePlan(String operator, String planName, String category, Double amount, Integer validityDays, String dataBenefit, String description) {
        this.operator = operator;
        this.planName = planName;
        this.category = category;
        this.amount = amount;
        this.validityDays = validityDays;
        this.dataBenefit = dataBenefit;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getValidityDays() {
        return validityDays;
    }

    public void setValidityDays(Integer validityDays) {
        this.validityDays = validityDays;
    }

    public String getDataBenefit() {
        return dataBenefit;
    }

    public void setDataBenefit(String dataBenefit) {
        this.dataBenefit = dataBenefit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
