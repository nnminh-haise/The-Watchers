package com.example.watch_selling.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.watch_selling.dtos.WatchInformationDto;
import com.example.watch_selling.model.Watch;

import jakarta.transaction.Transactional;


public interface WatchRepository extends PagingAndSortingRepository<Watch, UUID> {

    @SuppressWarnings("null")
    @Query(
        "SELECT new com.example.watch_selling.dtos.WatchInformationDto(w.id, w.name, w.price, w.quantity, w.description, w.status, w.photo, w.type.name, w.brand.name) FROM Watch w WHERE w.isDeleted = false"
    )
    public Page<Watch> findAll(Pageable pageable);

    /**
     * SQL:
     * SELECT 
     *  W.ID,
     *  W.NAME,
     *  W.PRICE,
     *  W.QUANTITY,
     *  W.DESCRIPTION,
     *  W.STATUS,
     *  W.PHOTO,
     *  (SELECT WATCH_TYPE.NAME FROM WATCH_TYPE WHERE WATCH_TYPE.ID = W.TYPE_ID) AS TYPE,
     *  (SELECT WATCH_BRAND.NAME FROM WATCH_BRAND WHERE WATCH_BRAND.ID = W.BRAND_ID) AS BRAND
     * FROM WATCH AS W
     * WHERE W.NAME = 'W1' AND W.IS_DELETED = FALSE
     * @param name
     * @return an optional WatchInformationDto object contains the watch's information
     */
    @Query("SELECT new com.example.watch_selling.dtos.WatchInformationDto(w.id, w.name, w.price, w.quantity, w.description, w.status, w.photo, w.type.name, w.brand.name) FROM Watch w WHERE w.name = :name AND w.isDeleted = false")
    public Optional<WatchInformationDto> findByName(@Param("name") String name);

    @Query("SELECT new com.example.watch_selling.dtos.WatchInformationDto(w.id, w.name, w.price, w.quantity, w.description, w.status, w.photo, w.type.name, w.brand.name) FROM Watch w WHERE w.id = :id AND w.isDeleted = false")
    public Optional<WatchInformationDto> findById(@Param("id") UUID id);

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


    @Transactional
    @Modifying
    @Query("UPDATE Watch w SET w.isDeleted = :status WHERE w.id = :id")
    public int updateDeleteStatus(@Param("id") UUID id, @Param("status") Boolean status);
}
