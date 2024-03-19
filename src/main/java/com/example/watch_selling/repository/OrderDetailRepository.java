package com.example.watch_selling.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.example.watch_selling.dtos.UpdateCartDetailDto;
import com.example.watch_selling.model.OrderDetail;

import jakarta.transaction.Transactional;

public interface OrderDetailRepository extends PagingAndSortingRepository<OrderDetail, UUID> {
        @Query("SELECT od FROM OrderDetail AS od WHERE od.isDeleted = false AND od.id = :id")
        public Optional<OrderDetail> findById(@Param("id") UUID id);

        @Query("SELECT od FROM OrderDetail AS od WHERE od.isDeleted = false AND od.order.id = :orderId ORDER BY od.price ASC")
        public Page<OrderDetail> findAllOrderDetailByOrderIdASC(@Param("orderId") UUID orderId, Pageable pageable);

        @Query("SELECT od FROM OrderDetail AS od WHERE od.isDeleted = false AND od.order.id = :orderId ORDER BY od.price DESC")
        public Page<OrderDetail> findAllOrderDetailByOrderIdDESC(@Param("orderId") UUID orderId, Pageable pageable);

        public OrderDetail save(OrderDetail orderDetail);

        @Modifying
        @Query(nativeQuery = true, value = "UPDATE order_detail SET " +
                        "price = :#{#orderDetail.price}, " +
                        "quantity = :#{#orderDetail.quantity} " +
                        "WHERE is_deleted = false AND id = :id")
        public Integer updateOrderDetailById(
                        @Param("id") UUID id,
                        @Param("orderDetail") UpdateCartDetailDto orderDetail);

        @Query("SELECT SUM(od.price * od.quantity) FROM OrderDetail AS od WHERE od.isDeleted = false AND od.order.id = :orderId")
        public Double totalOfOrder(@Param("orderId") UUID orderId);

        @Transactional
        @Modifying
        @Query("UPDATE OrderDetail AS od SET od.isDeleted = :status WHERE od.id = :id")
        public Integer updateOrderDetailDeleteStatusById(
                        @Param("id") UUID id,
                        @Param("status") Boolean status);
}
