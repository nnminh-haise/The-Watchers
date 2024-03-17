package com.example.watch_selling.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.watch_selling.dtos.UpdateCartDetailDto;
import com.example.watch_selling.model.CartDetail;
import com.example.watch_selling.model.Watch;

import jakarta.transaction.Transactional;

public interface CartDetailRepository extends JpaRepository<CartDetail, UUID> {
        @Query("SELECT cd FROM CartDetail AS cd WHERE cd.id = :id")
        @SuppressWarnings("null")
        public Optional<CartDetail> findById(@Param("id") UUID id);

        @Query("SELECT cd FROM CartDetail AS cd WHERE cd.cart.id = :cartId")
        public List<CartDetail> findAllDetailWithCartId(@Param("cartId") UUID cartId);

        @Query("SELECT cd.watch FROM CartDetail AS cd WHERE cd.id = :id")
        public Optional<Watch> findWatchByCartDetailId(@Param("id") UUID id);

        @Query("SELECT cd FROM CartDetail AS cd WHERE cd.cart.id = :cartId AND cd.watch.id = :watchId")
        public Optional<CartDetail> findByCartIdAndWatchId(@Param("cartId") UUID cartId,
                        @Param("watchId") UUID watchId);

        @Transactional
        @SuppressWarnings({ "null", "unchecked" })
        public CartDetail save(CartDetail cartDetail);

        @Modifying
        @Transactional
        @Query(nativeQuery = true, value = "UPDATE cart_detail " +
                        "SET price = :#{#dto.price}, quantity = :#{#dto.quantity} " +
                        "WHERE id = :id")
        public Integer updateCartDetailById(@Param("id") UUID id, @Param("dto") UpdateCartDetailDto dto);

        @Modifying
        @Transactional
        @Query(nativeQuery = true, value = "UPDATE cart_detail " +
                        "SET available = CASE WHEN cart_detail.quantity <= w.quantity THEN TRUE ELSE FALSE END " +
                        "FROM watch AS w " +
                        "WHERE w.is_deleted = false AND w.id = watch_id AND cart_id = :cartId")
        public Integer updateAvailabilityByCartIdAndQuantity(@Param("cartId") UUID cartId);

        @Modifying
        @Transactional
        @SuppressWarnings("null")
        @Query("DELETE FROM CartDetail AS cd WHERE cd.id = :id")
        public void deleteById(@Param("id") UUID id);
}
