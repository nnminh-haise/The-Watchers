package com.example.watch_selling.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.example.watch_selling.model.Order;

import jakarta.transaction.Transactional;

public interface OrderRepository extends PagingAndSortingRepository<Order, UUID> {
        @Query("SELECT o FROM Order AS o " +
                        "WHERE o.isDeleted = false " +
                        "AND o.account.id = :accountId " +
                        "AND (" +
                        "CASE WHEN NOT cast(:fromOrderDate as date) IS null THEN " +
                        "CASE WHEN NOT cast(:toOrderDate as date) IS null THEN " +
                        "CASE WHEN (o.orderDate >= :fromOrderDate AND o.orderDate <= :toOrderDate) THEN TRUE ELSE FALSE END "
                        +
                        "ELSE TRUE END " +
                        "ELSE TRUE END" +
                        ") ORDER BY o.orderDate ASC")
        public List<Order> findOrdersByAccountIdAndOrderDateASC(
                        @Param("accountId") UUID accountId,
                        @Param("fromOrderDate") LocalDate fromOrderDate,
                        @Param("toOrderDate") LocalDate toOrderDate,
                        Pageable pageable);

        @Query("SELECT o FROM Order AS o " +
                        "WHERE o.isDeleted = false " +
                        "AND o.account.id = :accountId " +
                        "AND (" +
                        "CASE WHEN NOT cast(:fromOrderDate as date) IS null THEN " +
                        "CASE WHEN NOT cast(:toOrderDate as date) IS null THEN " +
                        "CASE WHEN (o.orderDate >= :fromOrderDate AND o.orderDate <= :toOrderDate) THEN TRUE ELSE FALSE END "
                        +
                        "ELSE TRUE END " +
                        "ELSE TRUE END" +
                        ") ORDER BY o.orderDate DESC")
        public List<Order> findOrdersByAccountIdAndOrderDateDESC(
                        @Param("accountId") UUID accountId,
                        @Param("fromOrderDate") LocalDate fromOrderDate,
                        @Param("toOrderDate") LocalDate toOrderDate,
                        Pageable pageable);

        @Query("SELECT o FROM Order AS o WHERE o.isDeleted = false AND o.id = :id")
        public Optional<Order> findById(@Param("id") UUID id);

        @Query("SELECT o FROM Order AS o WHERE o.isDeleted = false AND o.orderDate = :date")
        public Optional<List<Order>> findByOrderDate(@Param("date") LocalDate date);

        public Order save(Order order);

        @Query(nativeQuery = true, value = "UPDATE orders SET " +
                        "order_date = :#{#updateOrder.orderDate}, " +
                        "name = :#{#updateOrder.name}, " +
                        "address = :#{#updateOrder.address}, " +
                        "phonenumber = :#{#updateOrder.phoneNumber}, " +
                        "delivery_date = :#{#updateOrder.deliveryDate} " +
                        "WHERE is_deleted = false AND id = :id")
        @Modifying
        @Transactional
        public Integer updateById(@Param("id") UUID id, @Param("updateOrder") Order order);

        @Query("UPDATE Order AS o SET o.isDeleted = :status WHERE o.id = :id")
        @Modifying
        @Transactional
        public void updateDeleteStatus(@Param("id") UUID id, @Param("status") Boolean status);
}
