package com.example.watch_selling.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import com.example.watch_selling.model.Account;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    @NonNull
    public List<Account> findAll();

    @Query("SELECT a FROM Account a WHERE a.email = ?1")
    public Optional<Account> findByEmail(String email);
}
