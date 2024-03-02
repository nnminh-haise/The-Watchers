package com.example.watch_selling.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.watch_selling.model.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID>{
    @Query("SELECT i FROM Invoice AS i WHERE i.deleteStatus = false")
    @SuppressWarnings("null")
    public List<Invoice> findAll();

    @Query("SELECT i FROM Invoice AS i WHERE i.deleteStatus = false AND i.id = :id")
    @SuppressWarnings("null")
    public Optional<Invoice> findById(@Param("id") UUID id);

    @Query("SELECT i FROM Invoice AS i WHERE i.deleteStatus = false AND i.order.id = :orderId")
    public List<Invoice> findAllInvoiceWithOrderID(@Param("orderId") UUID orderId);

    @SuppressWarnings({ "null", "unchecked" })
    public Invoice save(@Param("invoice") Invoice invoice);

    @Query(
        nativeQuery = true,
        value = "UPDATE invoice AS i SET " +
                    "create_date = :#{#updateInvoice.createDate}, " +
                    "total = :#{#updateInvoice.total}, " +
                    "tax_code = :#{#updateInvoice.taxCode}, " +
                    "order_id = :#{#updateInvoice.order.id}" +
                "WHERE is_deleted = false AND id = :id"
    )
    public Integer updateInvoiceById(
        @Param("id") UUID id,
        @Param("updateInvoice") Invoice updateInvoice
    );

    @Query("UPDATE Invoice AS i SET i.deleteStatus = :status WHERE i.id = :id")
    public Integer updateDeleteStatusById(@Param("id") UUID id, @Param("status") Boolean status);
}
