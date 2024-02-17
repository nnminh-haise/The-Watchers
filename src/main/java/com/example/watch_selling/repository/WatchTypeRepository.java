package com.example.watch_selling.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.watch_selling.model.WatchType;

public interface WatchTypeRepository extends JpaRepository<WatchType, UUID> {
    @SuppressWarnings("null")
    @Query(
        nativeQuery = true,
        value = "SELECT * FROM watch_type WHERE id = :id"
    )
    public Optional<WatchType> findById(
        @Param("id") UUID id
    );

    @Query(
        nativeQuery = true,
        value = "SELECT * FROM watch_type WHERE name = :name"
    )
    public Optional<WatchType> findByName(
        @Param("name") String name
    );

    @Query(
        nativeQuery = true,
        value = "UPDATE watch_type SET name = :name WHERE id = :id"
    )
    public void updateNameById(
        @Param("id") UUID id,
        @Param("name") String name
    );

    @Query(
        nativeQuery = true,
        value = "UPDATE watch_type SET is_deleted = :status WHERE id = :id"
    )
    public Integer updateDeleteStatusbyId(
        @Param("id") UUID id,
        @Param("status") Boolean status
    );

    @SuppressWarnings({ "null", "unchecked" })
    public WatchType save(WatchType watchType);
}
