package com.example.watch_selling.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.watch_selling.model.CartDetail;

import jakarta.transaction.Transactional;

public interface CartDetailRepository extends JpaRepository<CartDetail, UUID> {
    @Query("SELECT cd FROM CartDetail AS cd WHERE cd.id = :id")
    @SuppressWarnings("null")
    public Optional<CartDetail> findById(@Param("id") UUID id);

    @SuppressWarnings("null")
    @Query("SELECT cd FROM CartDetail AS cd")
    public List<CartDetail> findAll();

    @Query("SELECT cd FROM CartDetail AS cd WHERE cd.watch.id = :watchId")
    public List<CartDetail> findAllDetailWithWatchId(@Param("watchId") UUID watchId);

    @Query("SELECT cd FROM CartDetail AS cd WHERE cd.cart.id = :cartId")
    public List<CartDetail> findAllDetailWithCartId(@Param("cartId") UUID cartId);

    @Query("SELECT cd FROM CartDetail AS cd WHERE cd.cart.id = :cartId AND cd.watch.id = :watchId")
    public Optional<CartDetail> findCartDetailWithWatchAndCartId(
        @Param("cartId") UUID cartId, @Param("watchId") UUID watchId
    );

    @SuppressWarnings({ "unchecked", "null" })
    public CartDetail save(CartDetail cartDetail);

    @Modifying
    @Transactional
    @Query("UPDATE CartDetail AS cd SET cd.price = :price WHERE cd.id = :id")
    public Integer updateCartDetailPriceById(
        @Param("id") UUID id,
        @Param("price") Double price
    );

    @Modifying
    @Transactional
    @Query("UPDATE CartDetail AS cd SET cd.quantity = :quantity WHERE cd.id = :id")
    public Integer updateCartDetailQuantityById(
        @Param("id") UUID id,
        @Param("quantity") Integer quantity
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM CartDetail AS cd WHERE cd.id = :id")
    @SuppressWarnings("null")
    public void deleteById(@Param("id") UUID id);
}
