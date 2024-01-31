package com.example.watch_selling.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.watch_selling.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID>{
    @Query("SELECT c FROM Customer c WHERE c.email = ?1")
    public Optional<Customer> findByEmail(String email);

    public Customer save(Customer customer);

    // public Customer update(Customer customer);

    @Query("SELECT c.cmnd FROM Customer c WHERE c.cmnd = ?1")
    public Optional<String> findByCmnd(String cmnd);
}