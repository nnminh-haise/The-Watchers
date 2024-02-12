package com.example.watch_selling.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.watch_selling.model.WatchType;

public interface WatchTypeRepository extends JpaRepository<WatchType, UUID> {
    @SuppressWarnings("null")
    @Query(
        value = "SELECT * FROM watch_type WHERE id = :id",
        nativeQuery = true
    )
    public Optional<WatchType> findById(UUID id);

    @Query(
        value = "SELECT * FROM watch_type WHERE name = :name",
        nativeQuery = true
    )
    public Optional<WatchType> findByName(String name);
}
