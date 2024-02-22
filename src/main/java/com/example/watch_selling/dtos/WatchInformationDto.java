package com.example.watch_selling.dtos;

import java.util.UUID;

import com.example.watch_selling.model.Watch;
import com.example.watch_selling.model.WatchBrand;
import com.example.watch_selling.model.WatchType;

public class WatchInformationDto {
    private UUID id;

    private String name;

    private Double price;

    private Integer quantity;

    private String description;

    private String status;

    private String photo;

    private String type;

    private String brand;

    public WatchInformationDto() {

    }

    public WatchInformationDto(UUID id, String name, Double price, Integer quantity, String description, String status, String photo, String type, String brand) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.status = status;
        this.photo = photo;
        this.type = type;
        this.brand = brand;
    }

    public WatchInformationDto(String name, Double price, Integer quantity, String description, String status, String photo, String type, String brand) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.status = status;
        this.photo = photo;
        this.type = type;
        this.brand = brand;
    }

    public Watch toModel(Boolean deleteStatus, WatchType watchType, WatchBrand watchBrand) {
        return new Watch(
            this.id,
            this.name,
            this.price,
            this.quantity,
            this.description,
            this.status,
            this.photo,
            deleteStatus,
            watchType,
            watchBrand
        );
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

    public String getType() {
        return this.type;
    }

    public void setYype(String type) {
        this.type = type;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Watch build() {
        return new Watch(id, name, price, quantity, description, status, photo);
    }
}
