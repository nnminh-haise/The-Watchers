package com.example.watch_selling.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.watch_selling.model.Cart;

import jakarta.transaction.Transactional;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    @SuppressWarnings("null")
    @Query("SELECT c FROM Cart AS c WHERE c.isDeleted = false AND c.id = :id")
    public Optional<Cart> findById(@Param("id") UUID id);

    @Query("SELECT c FROM Cart AS c WHERE c.isDeleted = false AND c.account.id = :accountId")
    public Optional<Cart> findByAccountId(@Param("accountId") UUID accountId);

    @SuppressWarnings({ "null", "unchecked" })
    public Cart save(Cart cart);

    @Modifying
    @Transactional
    @SuppressWarnings("null")
    @Query("UPDATE Cart AS c SET c.isDeleted = true WHERE c.id = :id")
    public void deleteById(@Param("id") UUID id);

    @Modifying
    @Transactional
    @Query("UPDATE Cart c SET c.isDeleted = true WHERE c.account.id = :accountId")
    public void deleteByAccountId(@Param("accountId") UUID accountId);
}
