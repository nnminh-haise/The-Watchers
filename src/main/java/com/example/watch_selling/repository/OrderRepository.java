package com.example.watch_selling.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.watch_selling.dtos.OrderUpdateDto;
import com.example.watch_selling.model.Order;

import jakarta.transaction.Transactional;

public interface OrderRepository extends JpaRepository<Order, UUID>{
    @Query("SELECT o, o.account.id AS account_id FROM Order AS o WHERE o.isDeleted = false")
    @SuppressWarnings("null")
    public List<Order> findAll();

    @Query("SELECT o FROM Order AS o WHERE o.isDeleted = false AND o.id = :id")
    @SuppressWarnings("null")
    public Optional<Order> findById(@Param("id") UUID id);

    @Query("SELECT o FROM Order AS o WHERE o.isDeleted = false AND o.orderDate = :date")
    public Optional<List<Order>> findByOrderDate(@Param("date") Date date);

    @SuppressWarnings({ "null", "unchecked" })
    public Order save(Order order);

    @Query(
        nativeQuery = true,    
        value = "UPDATE orders SET " +
                    "order_date = :#{#updateOrder.orderDate}, " +
                    "name = :#{#updateOrder.name}, " +
                    "address = :#{#updateOrder.address}, " +
                    "phonenumber = :#{#updateOrder.phoneNumber}, " +
                    "delivery_date = :#{#updateOrder.deliveryDate} " +
                "WHERE is_deleted = false AND id = :id"
    )
    @Modifying
    @Transactional
    public Integer updateById(@Param("id") UUID id, @Param("updateOrder") Order order);

    @Query("UPDATE Order AS o SET o.isDeleted = :status WHERE o.id = :id")
    @Modifying
    @Transactional
    public void updateDeleteStatus(@Param("id") UUID id, @Param("status") Boolean status);
}
