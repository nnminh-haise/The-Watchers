package com.example.watch_selling.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.watch_selling.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {

    @SuppressWarnings("null")
    @Query("SELECT od FROM OrderDetail AS od WHERE od.isDeleted = false AND od.id = :id")
    public Optional<OrderDetail> findById(@Param("id") UUID id);

    @SuppressWarnings("null")
    @Query("SELECT od FROM OrderDetail AS od WHERE od.isDeleted = false")
    public List<OrderDetail> findAll();

    @SuppressWarnings({ "null", "unchecked" })
    public OrderDetail save(OrderDetail orderDetail);
}
