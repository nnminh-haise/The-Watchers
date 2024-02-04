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
    public Customer save(Customer customer);
    
    @Query("SELECT c FROM Customer c WHERE c.email = ?1")
    public Optional<Customer> findByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE c.cmnd = :citizenId")
    public Optional<Customer> findByCitizenId(String citizenId);

    @Query("SELECT c FROM Customer c WHERE c.cmnd = :taxCode")
    public Optional<Customer> findByTaxCode(String taxCode);

    @Query("SELECT c.email FROM Customer c WHERE c.sdt = :sdt")
    public Optional<String> findEmailByPhoneNumber(String sdt);

    @Query("SELECT c.email FROM Customer c WHERE c.masothue = :masothue")
    public Optional<String> findEmailByMasothue(String masothue);

    @Transactional
    @Modifying
    @Query("UPDATE Customer c SET c.ho = :#{#customer.ho}, c.ten = :#{#customer.ten}, c.gioitinh = :#{#customer.gioitinh}, c.ngaysinh = :#{#customer.ngaysinh}, c.diachi = :#{#customer.diachi}, c.sdt = :#{#customer.sdt}, c.hinhAnh = :#{#customer.hinhAnh} WHERE c.id = :id")
    public void update(UUID id, @Param("customer") Customer customer);

    @Transactional
    @Modifying
    @Query("UPDATE Customer c SET c.daXoa = :status WHERE c.id = :id")
    public void updateDeleteStatus(UUID id, Boolean status);
}