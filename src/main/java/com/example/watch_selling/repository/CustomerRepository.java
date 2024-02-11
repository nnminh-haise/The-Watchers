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

    // TODO: Rewrite these methods

    // @Query("SELECT c FROM Customer c WHERE c.citizen_id = :citizenId")
    // public Optional<Customer> findByCitizenId(String citizenId);

    // @Query("SELECT c FROM Customer c WHERE c.tax_code = :taxCode")
    // public Optional<Customer> findByTaxCode(String taxCode);

    // @Query("SELECT c.email FROM Customer c WHERE c.phone_number = :phoneNumber")
    // public Optional<String> findEmailByPhoneNumber(String phoneNumber);

    @Transactional
    @Modifying
    @Query(value = "UPDATE customer SET first_name = :#{#customer.firstName}, last_name = :#{#customer.lastName}, gender = :#{#customer.gender}, date_of_birth = :#{#customer.dateOfBirth}, address = :#{#customer.address}, phone_number = :#{#customer.phoneNumber}, photo = :#{#customer.photo} WHERE id = :id", nativeQuery = true)
    public void update(UUID id, @Param("customer") Customer customer);

    @Transactional
    @Modifying
    @Query(value = "UPDATE customer SET is_deleted = :isDeleted WHERE email = :email", nativeQuery = true)
    public void updateIsDeleted(String email, Boolean isDeleted);
}