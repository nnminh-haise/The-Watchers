package com.example.watch_selling.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.watch_selling.model.WatchBrand;

import jakarta.transaction.Transactional;

public interface WatchBrandRepository extends JpaRepository<WatchBrand, UUID> {
    @SuppressWarnings("null")
    @Query(nativeQuery = true, value = "SELECT * FROM watch_brand WHERE id = :id AND is_deleted = false")
    public Optional<WatchBrand> findById(
            @Param("id") UUID id);

    @Query(nativeQuery = true, value = "SELECT * FROM watch_brand WHERE name = :name AND is_deleted = false")
    public Optional<WatchBrand> findByName(
            @Param("name") String name);

    @SuppressWarnings("null")
    @Query(nativeQuery = true, value = "SELECT * FROM watch_brand WHERE is_deleted = false")
    public List<WatchBrand> findAll();

    @Query(nativeQuery = true, value = "UPDATE watch_brand SET name = :name WHERE id = :id AND is_deleted = false")
    @Modifying
    @Transactional
    public void updateNameById(
            @Param("id") UUID id,
            @Param("name") String name);

    @Query(nativeQuery = true, value = "UPDATE watch_brand SET is_deleted = :status WHERE id = :id")
    @Modifying
    @Transactional
    public Integer updateDeleteStatusbyId(
            @Param("id") UUID id,
            @Param("status") Boolean status);
}
