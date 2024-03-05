package com.example.watch_selling.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.watch_selling.model.Customer;

import jakarta.transaction.Transactional;

public interface CustomerRepository extends JpaRepository<Customer, UUID>{
    @SuppressWarnings("null")
    public List<Customer> findAll();

    @SuppressWarnings("null")
    @Query("SELECT c FROM Customer c WHERE c.isDeleted = false AND c.id = :id")
    public Optional<Customer> findById(@Param("id") UUID id);

    @Query("SELECT c FROM Customer c WHERE c.isDeleted = false AND c.account.id = :accountId")
    public Optional<Customer> findByAccountId(@Param("accountId") UUID accountId);

    @Query(
        "SELECT c FROM Customer AS c " +
        "WHERE c.isDeleted = false " +
            "AND c.citizenId = :citizenId " +
            "AND c.account.id != :excludeAccountId"
    )
    public Optional<Customer> existProfileWithCitizenId(
        @Param("citizenId") String citizenId,
        @Param("excludeAccountId") UUID excludeAccountId
    );

    @Query(
        "SELECT c FROM Customer AS c " +
        "WHERE c.isDeleted = false " +
            "AND c.phoneNumber = :phonenumber " +
            "AND c.account.id != :excludeAccountId"
    )
    public Optional<Customer> existProfileWithPhonenumber(
        @Param("phonenumber") String phonenumber,
        @Param("excludeAccountId") UUID excludeAccountId
    );

    @Query(
        "SELECT c FROM Customer AS c " +
        "WHERE c.isDeleted = false " +
            "AND c.taxCode = :taxCode " +
            "AND c.account.id != :excludeAccountId"
    )
    public Optional<Customer> existProfileWithTaxCode(
        @Param("taxCode") String taxCode,
        @Param("excludeAccountId") UUID excludeAccountId
    );

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
                "WHERE account_id = :accountId AND is_deleted = false"
    )
    public Integer updateCustomerProfileByAccountId(
        @Param("accountId") UUID accountId,
        @Param("customer") Customer updateCustomerProfile
    );

    @Transactional
    @Modifying
    @Query(
        "UPDATE Customer AS c SET " +
            "c.isDeleted = true " +
        "WHERE c.account.id = :accountId"
    )
    public void deleteProfileByAccountId(@Param("accountId") UUID accountId);
}