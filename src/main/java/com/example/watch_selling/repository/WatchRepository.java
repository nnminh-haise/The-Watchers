package com.example.watch_selling.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.watch_selling.dtos.WatchInformationDto;
import com.example.watch_selling.model.Watch;


public interface WatchRepository extends JpaRepository<Watch, UUID> {
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

}
