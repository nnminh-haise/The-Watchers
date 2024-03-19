package com.example.watch_selling.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.CreateInvoiceDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Invoice;
import com.example.watch_selling.model.Order;
import com.example.watch_selling.repository.InvoiceRepository;
import com.example.watch_selling.repository.OrderDetailRepository;
import com.example.watch_selling.repository.OrderRepository;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public ResponseDto<List<Invoice>> findAllInvoices(
            Integer page, Integer size,
            String dateSortBy, String fromDate, String toDate,
            String totalSortBy, String fromTotal, String toTotal) {
        ResponseDto<List<Invoice>> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        List<String> sortByValues = List.of("asc", "desc");
        Boolean flag = false;
        for (String value : sortByValues) {
            if (dateSortBy.equalsIgnoreCase(value)) {
                flag = true;
            }
        }
        if (!flag) {
            return res.setMessage("Invalid date sort by value!");
        }

        flag = false;
        for (String value : sortByValues) {
            if (totalSortBy.equalsIgnoreCase(value)) {
                flag = true;
            }
        }
        if (!flag) {
            return res.setMessage("Invalid total sort by value!");
        }

        try {
            LocalDate validFromDate = (fromDate == null) ? null : LocalDate.parse(fromDate);
            LocalDate validToDate = (toDate == null) ? null : LocalDate.parse(toDate);
            Double validFromTotal = (fromTotal == null) ? null : Double.parseDouble(fromTotal);
            Double validToTotal = (toTotal == null) ? null : Double.parseDouble(toTotal);

            Pageable paging = PageRequest.of(page, size);
            List<Invoice> invoices = invoiceRepository.findAllByDateAndTotal(
                    paging, dateSortBy, validFromDate, validToDate, totalSortBy, validFromTotal, validToTotal);

            if (invoices.isEmpty()) {
                return res
                        .setMessage("Cannot find any invoice!")
                        .setStatus(HttpStatus.NOT_FOUND);
            }

            return res
                    .setData(invoices)
                    .setMessage("Invoice found successfully!")
                    .setStatus(HttpStatus.OK);
        } catch (Exception e) {
            return res.setMessage(e.getMessage());
        }
    }

    public ResponseDto<Invoice> findById(UUID id) {
        ResponseDto<Invoice> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null) {
            return res.setMessage("Invalid ID!");
        }

        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if (!invoice.isPresent()) {
            return res
                    .setMessage("Cannot find any invoice with the given ID!")
                    .setStatus(HttpStatus.NOT_FOUND);
        }

        return res
                .setMessage("Invoice found successfully!")
                .setStatus(HttpStatus.OK)
                .setData(invoice.get());
    }

    public ResponseDto<Invoice> createNewInvoice(CreateInvoiceDto dto) {
        ResponseDto<Invoice> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (!CreateInvoiceDto.validateDto(dto)) {
            return res.setMessage("Invalid DTO");
        }

        Optional<Order> targetingOrder = orderRepository.findById(dto.getOrderId());
        if (!targetingOrder.isPresent()) {
            return res.setMessage("Cannot find any Order with the given order ID!");
        }

        Double totalPrice = orderDetailRepository.totalOfOrder(dto.getOrderId());
        Double priceAfterTax = totalPrice * 1.08;
        Invoice newInvoice = new Invoice(
                null, LocalDate.now(), priceAfterTax, dto.getTaxCode(), false, targetingOrder.get());

        Invoice invoice = invoiceRepository.save(newInvoice);

        return res
                .setData(invoice)
                .setStatus(HttpStatus.OK)
                .setMessage("Successful!");
    }

    public ResponseDto<String> updateDeleteStatusById(UUID id, Boolean status) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null) {
            return res.setMessage("Invalid ID!");
        }

        invoiceRepository.updateDeleteStatusById(id, status);
        return res
                .setStatus(HttpStatus.OK)
                .setMessage("Invoice updated successfully!");
    }
}
