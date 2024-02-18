package com.example.watch_selling.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.watch_selling.dtos.CartDetailsDto;
import com.example.watch_selling.model.Cart;

import jakarta.transaction.Transactional;

// TODO: Update query for taking cart detail handling the customer profile is not exist yet

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    @SuppressWarnings("null")
    @Query(
        nativeQuery = true,
        value = "SELECT * FROM cart WHERE is_deleted = false"
    )
    public List<Cart> findAll();

    @SuppressWarnings("null")
    @Query(
        nativeQuery = true,
        value = "SELECT * FROM cart WHERE id = :id AND is_deleted = false"
    )
    public Optional<Cart> findById(@Param("id") UUID id);

    @Query(
        nativeQuery = true,
        value = "SELECT * FROM cart WHERE account_id = :accountId AND is_deleted = false"
    )
    public Optional<Cart> findbyAccountId(@Param("accountId") UUID accountId);

    @SuppressWarnings({ "null", "unchecked" })
    public Cart save(Cart cart);

    @Query(
        nativeQuery = true,
        value = "UPDATE cart SET account_id = :accountId WHERE id = :id AND is_deleted = false"
    )
    @Modifying
    @Transactional
    public void updateAccountId(
        @Param("id") UUID id,
        @Param("accountId") UUID accountId
    );

    @Query(
        nativeQuery = true,
        value = "UPDATE cart SET is_deleted = :status WHERE id = :id"
    )
    @Modifying
    @Transactional
    public Integer updateDeleteStatus(
        @Param("id") UUID id,
        @Param("status") Boolean status
    );

    @Query(
        "SELECT new com.example.watch_selling.dtos.CartDetailsDto(" +
            "c.id, " +
            "c.account.id, " +
            "cus.firstName || ' ' || cus.lastName AS fullname, " +
            "0 AS item_count, " +
            "c.isDeleted) " +
        "FROM Cart c INNER JOIN Customer AS cus ON c.account.id = cus.account.id " +
        "WHERE c.isDeleted = false AND cus.isDeleted = false AND c.id = :id"
    )
    public Optional<CartDetailsDto> findCartDetailById(@Param("id") UUID id);
}
