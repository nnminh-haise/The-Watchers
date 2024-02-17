package com.example.watch_selling.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "watch")
public class Watch {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String status;

    @Column(nullable = true)
    private String photo;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "type_id", referencedColumnName = "id", unique = true, nullable = false)
    @JsonIgnore
    private WatchType type;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "brand_id", referencedColumnName = "id", unique = true, nullable = false)
    @JsonIgnore
    private WatchBrand brand;

    public Watch() {

    }

    public Watch(UUID id, String name, Double price, Integer quantity, String description, String status, String photo, Boolean isDeleted, WatchType type, WatchBrand brand) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.status = status;
        this.photo = photo;
        this.isDeleted = isDeleted;
        this.type = type;
        this.brand = brand;
    }

    public Watch(String name, Double price, Integer quantity, String description, String status, String photo, WatchType type, WatchBrand brand) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.status = status;
        this.photo = photo;
        this.type = type;
        this.brand = brand;
    }

    public Watch(UUID id, String name, Double price, Integer quantity, String description, String status, String photo) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.status = status;
        this.photo = photo;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public WatchType getType() {
        return this.type;
    }

    public void setType(WatchType type) {
        this.type = type;
    }

    public WatchBrand getBrand() {
        return this.brand;
    }

    public void setBrand(WatchBrand brand) {
        this.brand = brand;
    }
}
