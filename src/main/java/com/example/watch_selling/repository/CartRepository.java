package com.example.watch_selling.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.watch_selling.model.Cart;

import jakarta.transaction.Transactional;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    @SuppressWarnings("null")
    @Query(
        nativeQuery = true,
        value = "SELECT * FROM cart"
    )
    public List<Cart> findAll();

    @SuppressWarnings("null")
    @Query(
        nativeQuery = true,
        value = "SELECT * FROM cart WHERE id = :id"
    )
    public Optional<Cart> findById(@Param("id") UUID id);

    @Query(
        nativeQuery = true,
        value = "SELECT * FROM cart WHERE account_id = :accountId"
    )
    public Optional<Cart> findbyAccountId(@Param("accountId") UUID accountId);

    @SuppressWarnings({ "null", "unchecked" })
    public Cart save(Cart cart);

    @Query(
        nativeQuery = true,
        value = "UPDATE cart SET account_id = :accountId WHERE id = :id"
    )
    public void updateAccountId(
        @Param("id") UUID id,
        @Param("accountId") UUID accountId
    );

    @Transactional
    @Modifying
    @Query(
        nativeQuery = true,
        value = "UPDATE cart SET is_deleted = :status WHERE id = :id"
    )
    public Integer updateDeleteStatus(
        @Param("id") UUID id,
        @Param("status") Boolean status
    );
}