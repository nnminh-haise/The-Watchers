package com.example.watch_selling.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.watch_selling.dtos.CustomerProfileDto;
import com.example.watch_selling.model.Customer;

import jakarta.transaction.Transactional;

public interface CustomerRepository extends JpaRepository<Customer, UUID>{
    @SuppressWarnings("null")
    public List<Customer> findAll();

    @SuppressWarnings("null")
    @Query("SELECT c FROM Customer c WHERE c.id = :id AND c.isDeleted = false")
    public Optional<Customer> findById(@Param("id") UUID id);

    @Query("SELECT c FROM Customer AS c WHERE c.isDeleted = false AND c.account.email = :email")
    public Optional<Customer> findByEmail(@Param("email") String email);

    @Query("SELECT c FROM Customer c WHERE c.account.id = :accountId AND c.isDeleted = false")
    public Optional<Customer> findByAccountId(@Param("accountId") UUID accountId);

    @Query("SELECT c FROM Customer AS c WHERE c.isDeleted = false AND c.citizenId = :citizenId")
    public Optional<Customer> existProfileByCitizenId(@Param("citizenId") String citizenId);

    @Query("SELECT c FROM Customer AS c WHERE c.isDeleted = false AND c.phoneNumber = :phonenumber")
    public Optional<Customer> existProfileByPhonenumber(@Param("phonenumber") String phonenumber);
    
    @SuppressWarnings({ "null", "unchecked" })
    public Customer save(Customer customer);

    @Transactional
    @Modifying
    @Query(
        nativeQuery = true,
        value = "UPDATE customer SET " +
                    "first_name = :#{#customer.firstName}, " +
                    "last_name = :#{#customer.lastName}, " +
                    "gender = :#{#customer.gender}, " +
                    "date_of_birth = :#{#customer.dateOfBirth}, " +
                    "address = :#{#customer.address}, " +
                    "phone_number = :#{#customer.phoneNumber}, " +
                    "photo = :#{#customer.photo} " +
                "WHERE id = :id AND is_deleted = false"
    )
    public Customer updateCustomerProfileById(
        @Param("id") UUID id,
        @Param("customer") CustomerProfileDto updateCustomerProfile
    );

    @Transactional
    @Modifying
    @Query("UPDATE Customer AS c SET c.isDeleted = :status WHERE c.id = :id")
    public void updateDeleteStatusById(@Param("id") UUID id, @Param("status") Boolean status);
}