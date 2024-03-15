package com.example.watch_selling.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "watch_brand")
public class WatchBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "is_deleted")
    @JsonIgnore
    private Boolean isDeleted;

    public WatchBrand() {

    }

    public WatchBrand(UUID id, String name, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;
    }

    public WatchBrand(String name, Boolean isDeleted) {
        this.name = name;
        this.isDeleted = isDeleted;
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

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
