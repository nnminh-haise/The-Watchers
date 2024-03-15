package com.example.watch_selling.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.watch_selling.model.Cart;

import jakarta.transaction.Transactional;

@Repository
public interface CartRepository extends PagingAndSortingRepository<Cart, UUID> {
    @SuppressWarnings("null")
    @Query("SELECT c FROM Cart as c WHERE c.isDeleted = false")
    public Page<Cart> findAll(Pageable pageable);

    @Query("SELECT c FROM Cart AS c WHERE c.id = :id AND c.isDeleted = false")
    public Optional<Cart> findById(@Param("id") UUID id);

    @Query("SELECT c FROM Cart AS c WHERE c.account.id = :accountId AND c.isDeleted = false")
    public Optional<Cart> findbyAccountId(@Param("accountId") UUID accountId);

    public Cart save(Cart cart);

    @Query("UPDATE Cart AS c SET c.isDeleted = true WHERE c.id = :id")
    @Modifying
    @Transactional
    public void deleteById(@Param("id") UUID id);

    @Query(nativeQuery = true, value = "UPDATE cart SET is_deleted = true WHERE account_id = :accountId")
    @Modifying
    @Transactional
    public void deleteByAccountId(@Param("accountId") UUID accountId);
}
