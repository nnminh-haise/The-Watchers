package com.example.watch_selling.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.watch_selling.model.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Long>{
    @Query("SELECT c FROM Customer c WHERE c.email = ?1")
    public Optional<Customer> findByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE c.id = ?1")
    public Customer save(Customer customer);
}