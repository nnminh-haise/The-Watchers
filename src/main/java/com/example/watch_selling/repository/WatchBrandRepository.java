package com.example.watch_selling.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.watch_selling.model.WatchBrand;

public interface WatchBrandRepository extends JpaRepository<WatchBrand, UUID> {
    @SuppressWarnings("null")
    @Query("SELECT wb FROM WatchBrand wb WHERE wb.id = :id")
    public Optional<WatchBrand> findById(@Param("id") UUID id);

    @Query("SELECT wb FROM WatchBrand wb WHERE wb.name = :name")
    public Optional<WatchBrand> findByName(@Param("name") String name);
}
