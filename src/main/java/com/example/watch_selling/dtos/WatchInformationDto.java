package com.example.watch_selling.dtos;

import java.util.UUID;

import com.example.watch_selling.model.Watch;

public class WatchInformationDto {
    private UUID id;

    private String name;

    private Double price;

    private Integer quantity;

    private String description;

    private String status;

    private String photo;

    private UUID typeId;

    private UUID brandId;

    public WatchInformationDto() {

    }

    public WatchInformationDto(UUID id, String name, Double price, Integer quantity, String description, String status, String photo, UUID typeId, UUID brandId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.status = status;
        this.photo = photo;
        this.typeId = typeId;
        this.brandId = brandId;
    }

    public WatchInformationDto(String name, Double price, Integer quantity, String description, String status, String photo, UUID typeId, UUID brandId) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.status = status;
        this.photo = photo;
        this.typeId = typeId;
        this.brandId = brandId;
    }

    public WatchInformationDto(UUID id, String name, Double price, Integer quantity, String description, String status, String photo) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.status = status;
        this.photo = photo;
    }

    public WatchInformationDto(Watch watch) {
        this.id = watch.getId();
        this.name = watch.getName();
        this.price = watch.getPrice();
        this.quantity = watch.getQuantity();
        this.description = watch.getDescription();
        this.status = watch.getStatus();
        this.photo = watch.getPhoto();
        this.typeId = watch.getType().getId();
        this.brandId = watch.getBrand().getId();
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

    public UUID getTypeId() {
        return this.typeId;
    }

    public void setTypeId(UUID typeId) {
        this.typeId = typeId;
    }

    public UUID getBrandId() {
        return this.brandId;
    }

    public void setBrandId(UUID brandId) {
        this.brandId = brandId;
    }
}
