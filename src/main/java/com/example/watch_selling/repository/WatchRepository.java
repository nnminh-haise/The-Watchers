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
    @Query(
        "SELECT w FROM Watch AS w " +
        "WHERE w.isDeleted = false AND (cast(:typeId as uuid) IS null OR w.type.id = :typeId) AND (cast(:brandId as uuid) IS null OR w.brand.id = :brandId)" +
        "ORDER BY w.price ASC"
    )
    public List<Watch> findWatchesByTypeASC(
        @Param("typeId") UUID typeId,
        @Param("brandId") UUID brandId,
        Pageable pageable
    );

    @Query(
        "SELECT w FROM Watch AS w " +
        "WHERE w.isDeleted = false AND (cast(:typeId as uuid) IS null OR w.type.id = :typeId) AND (cast(:brandId as uuid) IS null OR w.brand.id = :brandId)" +
        "ORDER BY w.price DESC"
    )
    public List<Watch> findWatchesByTypeDESC(
        @Param("typeId") UUID typeId,
        @Param("brandId") UUID brandId,
        Pageable pageable
    );

    @Query("SELECT w FROM Watch w WHERE w.name = :name AND w.isDeleted = false")
    public Optional<Watch> findByName(@Param("name") String name);

    @Query("SELECT w FROM Watch AS w WHERE w.isDeleted = false AND w.id = :id")
    public Optional<Watch> findById(@Param("id") UUID id);

    public Watch save(Watch watch);

    @Transactional
    @Modifying
    @Query(
        value = "UPDATE watch SET " + 
                    "name = :#{#information.name}, " +
                    "price = :#{#information.price}, " +
                    "quantity = :#{#information.quantity}, " +
                    "description = :#{#information.description}, " +
                    "status = :#{#information.status}, " +
                    "photo = :#{#information.photo}, " +
                    "type_id = (SELECT watch_type.id FROM watch_type WHERE watch_type.name = :#{#information.type}), " +
                    "brand_id = (SELECT watch_brand.id FROM watch_brand WHERE watch_brand.name = :#{#information.brand}) " +
                "WHERE id = :id",
        nativeQuery = true
    )
    public void updateWatchById(@Param("id") UUID id, @Param("information") WatchInformationDto information);

    @Query("UPDATE Watch w SET w.price = :price WHERE w.id = :id AND w.isDeleted = false")
    @Modifying
    @Transactional
    public Integer updateWatchPriceById(@Param("id") UUID id, @Param("price") Double price);

    @Query("UPDATE Watch w SET w.quantity = :quantity WHERE w.id = :id AND w.isDeleted = false")
    @Modifying
    @Transactional
    public Integer updateWatchQuantityById(@Param("id") UUID id, @Param("quantity") Integer quantity);

    @Query("UPDATE Watch w SET w.quantity = w.quantity - :quantity WHERE w.id = :id AND w.isDeleted = false")
    @Modifying
    @Transactional
    public Integer decreaseWatchQuantityById(@Param("id") UUID id, @Param("quantity") Integer quantity);

    @Modifying
    @Transactional
    @Query("UPDATE Watch w SET w.isDeleted = :status WHERE w.id = :id")
    public int updateDeleteStatus(@Param("id") UUID id, @Param("status") Boolean status);

    @Query("SELECT w.type FROM Watch AS w WHERE w.id = :id")
    public WatchType findWatchTypeByWatchId(@Param("id") UUID id);

    @Query("SELECT w.brand FROM Watch AS w WHERE w.id = :id")
    public WatchBrand findWatchBrandByWatchId(@Param("id") UUID id);
}
