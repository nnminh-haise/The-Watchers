package com.example.watch_selling.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.watch_selling.model.Watch;

public interface WatchRepository extends JpaRepository<Watch, UUID> {
    @Query(
        value = "SELECT * FROM watch WHERE is_deleted = false",
        nativeQuery = true
    )
    public List<Watch> findAllWatchesInformation();
}
