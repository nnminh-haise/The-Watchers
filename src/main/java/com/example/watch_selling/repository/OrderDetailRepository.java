package com.example.watch_selling.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.watch_selling.dtos.OrderDetailDto;
import com.example.watch_selling.model.OrderDetail;

import jakarta.transaction.Transactional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {

    @SuppressWarnings("null")
    @Query("SELECT od FROM OrderDetail AS od WHERE od.isDeleted = false AND od.id = :id")
    public Optional<OrderDetail> findById(@Param("id") UUID id);

    @SuppressWarnings("null")
    @Query("SELECT od FROM OrderDetail AS od WHERE od.isDeleted = false")
    public List<OrderDetail> findAll();

    @SuppressWarnings({ "null", "unchecked" })
    public OrderDetail save(OrderDetail orderDetail);

    @Transactional
    @Modifying
    @Query(
        nativeQuery = true,
        value = "UPDATE order_detail SET " +
                    "order_id = :#{#orderDetail.order.id}, " +
                    "watch_id = :#{#orderDetail.watch.id}, " +
                    "price = :#{#orderDetail.price}, " +
                    "quantity = :#{#orderDetail.quantity} " +
                "WHERE is_deleted = false AND id = :id"
    )
    public OrderDetail updateOrderDetailById(
        @Param("id") UUID id,
        @Param("orderDetail") OrderDetailDto orderDetail
    );

    @Transactional
    @Modifying
    @Query("UPDATE OrderDetail AS od SET od.isDeleted = :status WHERE od.id = :id")
    public OrderDetail updateOrderDetailDeleteStatusById(
        @Param("id") UUID id,
        @Param("status") Boolean status
    );
}
