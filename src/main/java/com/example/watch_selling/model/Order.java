package com.example.watch_selling.model;

import java.util.Date;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "order_date", nullable = false)
    private Date orderDate;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(name = "phonenumber", nullable = false)
    private String phoneNumber;

    @Column(name = "delivery_date", nullable = false)
    private Date deliveryDate;

    @Column(nullable = false)
    private String status;

    @Column(name = "is_deleted", nullable = false)
    @JsonIgnore
    private Boolean isDeleted;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "account_id", referencedColumnName = "id", unique = true, nullable = false)
    private Account account;
}
