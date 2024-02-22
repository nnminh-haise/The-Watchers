package com.example.watch_selling.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.watch_selling.model.Order;

public interface OrderRepository extends JpaRepository<Order, UUID>{
    @SuppressWarnings("null")
    @Query("SELECT o, o.account.id AS account_id FROM Order AS o WHERE o.isDeleted = false")
    public List<Order> findAll();

    @SuppressWarnings({ "null", "unchecked" })
    public Order save(Order order);
}
