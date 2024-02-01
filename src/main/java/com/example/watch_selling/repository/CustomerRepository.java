package com.example.watch_selling.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.watch_selling.model.Customer;

import jakarta.transaction.Transactional;

public interface CustomerRepository extends JpaRepository<Customer, UUID>{
    @Query("SELECT c FROM Customer c WHERE c.email = ?1")
    public Optional<Customer> findByEmail(String email);

    public Customer save(Customer customer);

    @Query("SELECT c.email FROM Customer c WHERE c.account.id = :accountId")
    public Optional<String> findEmailByAccountID(UUID accountId);

    @Query("SELECT c.email FROM Customer c WHERE c.sdt = :sdt")
    public Optional<String> findEmailByPhoneNumber(String sdt);

    @Query("SELECT c.email FROM Customer c WHERE c.masothue = :masothue")
    public Optional<String> findEmailByMasothue(String masothue);

    @Transactional
    @Modifying
    @Query("UPDATE Customer c SET c.ho = :#{#customer.ho}, c.ten = :#{#customer.ten}, c.gioitinh = :#{#customer.gioitinh}, c.ngaysinh = :#{#customer.ngaysinh}, c.diachi = :#{#customer.diachi}, c.sdt = :#{#customer.sdt}, c.masothue = :#{#customer.masothue}, c.hinhAnh = :#{#customer.hinhAnh} WHERE c.email = :#{#customer.email} AND c.account.id = :accountId")
    public void update(UUID accountId, @Param("customer") Customer customer);
}