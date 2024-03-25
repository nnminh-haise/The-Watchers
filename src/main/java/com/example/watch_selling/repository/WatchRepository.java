package com.example.watch_selling.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.watch_selling.dtos.WatchInformationDto;
import com.example.watch_selling.model.Watch;
import com.example.watch_selling.model.WatchBrand;
import com.example.watch_selling.model.WatchType;

import jakarta.transaction.Transactional;

public interface WatchRepository extends PagingAndSortingRepository<Watch, UUID> {
        @Query("SELECT w FROM Watch AS w " +
                        "WHERE w.isDeleted = false " +
                        "AND (:typeIds IS NULL OR w.type.id in :typeIds) " +
                        "AND (:brandIds IS NULL OR w.brand.id in :brandIds) " +
                        "ORDER BY " +
                        "CASE WHEN :sortBy = 'asc' THEN w.price END ASC, " +
                        "CASE WHEN :sortBy = 'desc' THEN w.price END DESC ")
        public List<Watch> findWatchesByTypeAndBrand(
                        @Param("typeIds") List<UUID> typeIds,
                        @Param("brandIds") List<UUID> brandIds,
                        @Param("sortBy") String sortBy,
                        Pageable pageable);

        @Query("SELECT w FROM Watch w WHERE w.name = :name AND w.isDeleted = false")
        public Optional<Watch> findByName(@Param("name") String name);

        @Query("SELECT w FROM Watch AS w WHERE w.isDeleted = false AND w.id = :id")
        public Optional<Watch> findById(@Param("id") UUID id);

        public Watch save(Watch watch);

        @Transactional
        @Modifying
        @Query(value = "UPDATE watch SET " +
                        "name = :#{#information.name}, " +
                        "price = :#{#information.price}, " +
                        "quantity = :#{#information.quantity}, " +
                        "description = :#{#information.description}, " +
                        "status = :#{#information.status}, " +
                        "photo = :#{#information.photo}, " +
                        "type_id = (SELECT watch_type.id FROM watch_type WHERE watch_type.name = :#{#information.typeName}), "
                        +
                        "brand_id = (SELECT watch_brand.id FROM watch_brand WHERE watch_brand.name = :#{#information.brandName}) "
                        +
                        "WHERE id = :id", nativeQuery = true)
        public void updateWatchById(@Param("id") UUID id, @Param("information") WatchInformationDto information);

        // @Modifying
        // @Query("UPDATE Watch w SET w.price = :price WHERE w.id = :id AND w.isDeleted
        // = false")
        // public Integer updateWatchPriceById(@Param("id") UUID id, @Param("price")
        // Double price);

        @Modifying
        @Query("UPDATE Watch w SET w.quantity = :newQuantity WHERE w.id = :id AND w.isDeleted = false")
        public Integer updateWatchQuantityById(@Param("id") UUID id, @Param("newQuantity") Integer newQuantity);

        @Modifying
        @Transactional
        @Query("UPDATE Watch w SET w.isDeleted = :status WHERE w.id = :id")
        public int updateDeleteStatus(@Param("id") UUID id, @Param("status") Boolean status);

        @Query("SELECT w.type FROM Watch AS w WHERE w.id = :id")
        public WatchType findWatchTypeByWatchId(@Param("id") UUID id);

        @Query("SELECT w.brand FROM Watch AS w WHERE w.id = :id")
        public WatchBrand findWatchBrandByWatchId(@Param("id") UUID id);
}
