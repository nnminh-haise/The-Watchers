package com.example.watch_selling.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.example.watch_selling.model.Invoice;

public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, UUID> {
    @Query("SELECT i FROM Invoice AS i " +
            "WHERE i.deleteStatus = false " +
            "AND ( " +
            "CASE WHEN NOT CAST(:fromDate AS date) IS null THEN " +
            "CASE WHEN NOT CAST(:toDate AS date) IS null THEN " +
            "CASE WHEN :fromDate <= i.createDate AND i.createDate <= :toDate THEN TRUE " +
            "ELSE FALSE END " +
            "ELSE TRUE END " +
            "ELSE TRUE END " +
            ") " +
            "AND ( " +
            "CASE WHEN NOT CAST(:fromTotal AS double) IS null THEN " +
            "CASE WHEN NOT CAST(:toTotal AS double) IS null THEN " +
            "CASE WHEN :fromTotal <= i.total AND i.total <= :toTotal THEN TRUE " +
            "ELSE FALSE END " +
            "ELSE TRUE END " +
            "ELSE TRUE END " +
            ") " +
            "ORDER BY " +
            "CASE WHEN :dateSortBy = 'asc' THEN i.createDate END ASC, " +
            "CASE WHEN :dateSortBy = 'desc' THEN i.createDate END DESC, " +
            "CASE WHEN :totalSortBy = 'asc' THEN i.total END ASC, " +
            "CASE WHEN :totalSortBy = 'desc' THEN i.total ENd DESC")
    public List<Invoice> findAllByDateAndTotal(
            Pageable page,
            String dateSortBy, LocalDate fromDate, LocalDate toDate,
            String totalSortBy, Double fromTotal, Double toTotal);

    @Query("SELECT i FROM Invoice AS i WHERE i.deleteStatus = false AND i.id = :id")
    public Optional<Invoice> findById(@Param("id") UUID id);

    @Query("SELECT i FROM Invoice AS i WHERE i.deleteStatus = false AND i.order.id = :orderId")
    public List<Invoice> findAllInvoiceWithOrderID(@Param("orderId") UUID orderId);

    public Invoice save(@Param("invoice") Invoice invoice);

    @Query("UPDATE Invoice AS i SET i.deleteStatus = :status WHERE i.id = :id")
    public Integer updateDeleteStatusById(@Param("id") UUID id, @Param("status") Boolean status);
}
