package com.example.watch_selling.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import com.example.watch_selling.model.Account;

import jakarta.transaction.Transactional;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    @NonNull
    @Query("SELECT a FROM Account AS a WHERE a.isDeleted = false")
    public List<Account> findAll();

    @Query("SELECT a FROM Account AS a WHERE a.isDeleted = false AND a.email = :email")
    public Optional<Account> findByEmail(@Param("email") String email);

    @Query("UPDATE Account AS a SET a.isDeleted = :status WHERE a.email = :email")
    @Transactional
    @Modifying
    public Integer updateDeleteStatus(
            @Param("email") String email,
            @Param("status") Boolean status);

    @Query("UPDATE Account AS a SET a.isDeleted = true WHERE a.id = :id")
    @Transactional
    @Modifying
    @SuppressWarnings("null")
    public void deleteById(@Param("id") UUID id);

    @SuppressWarnings({ "null", "unchecked" })
    public Account save(Account account);
}
