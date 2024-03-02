package com.example.watch_selling.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.InvoiceUpdateDto;
import com.example.watch_selling.dtos.OrderDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Invoice;
import com.example.watch_selling.model.Order;
import com.example.watch_selling.repository.InvoiceRepository;
import com.example.watch_selling.repository.OrderRepository;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private OrderRepository orderRepository;

    public ResponseDto<List<Invoice>> findAllInvoices() {
        ResponseDto<List<Invoice>> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        List<Invoice> invoices = invoiceRepository.findAll();
        if (invoices.isEmpty()) {
            return res
                .setMessage("Cannot find any invoice!")
                .setStatus(HttpStatus.NOT_FOUND);
        }

        return res
            .setData(invoices)
            .setMessage("Invoice found successfully!")
            .setStatus(HttpStatus.OK);
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

    public ResponseDto<Invoice> createNewInvoice(InvoiceUpdateDto newInvoice) {
        ResponseDto<Invoice> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (newInvoice == null) {
            return res.setMessage("Invalid invoide information!");
        }

        ResponseDto<InvoiceUpdateDto> dtoValidateResponse = InvoiceUpdateDto.validDto(newInvoice);
        if (!dtoValidateResponse.getStatus().equals(HttpStatus.OK)) {
            return res
                .setMessage(dtoValidateResponse.getMessage())
                .setStatus(dtoValidateResponse.getStatus());
        }

        Optional<Order> orderOfInvoice = orderRepository.findById(newInvoice.getOrderId());
        if (!orderOfInvoice.isPresent()) {
            return res.setMessage("Cannot find any Order with the given order ID!");
        }

        Invoice invoice = invoiceRepository.save(
            InvoiceUpdateDto.toModel(newInvoice, false, orderOfInvoice.get()).get()
        );

        return res
            .setMessage("New incoide created successfully!")
            .setStatus(HttpStatus.OK)
            .setData(invoice);
    }

    public ResponseDto<Invoice> updateInvoiceById(UUID id, InvoiceUpdateDto updateInvoice) {
        ResponseDto<Invoice> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null) {
            return res.setMessage("Invalid ID!");
        }

        Optional<Invoice> targettingInvoice = invoiceRepository.findById(id);
        if (!targettingInvoice.isPresent()) {
            return res
                .setMessage("Cannot find any invoice with the given ID!")
                .setStatus(HttpStatus.NOT_FOUND);
        }

        ResponseDto<InvoiceUpdateDto> dtoValidateResponse = InvoiceUpdateDto.validDto(updateInvoice);
        if (!dtoValidateResponse.getStatus().equals(HttpStatus.OK)) {
            return res
                .setMessage(dtoValidateResponse.getMessage())
                .setStatus(dtoValidateResponse.getStatus());
        }

        Optional<Order> invoiceOrder = orderRepository.findById(updateInvoice.getOrderId());
        if (!invoiceOrder.isPresent()) {
            return res
                .setMessage("Cannot find any order with the given order ID!")
                .setStatus(HttpStatus.NOT_FOUND);
        }

        Invoice updatedInvoice = targettingInvoice.get();
        updatedInvoice.setCreateDate(OrderDto.parseDate(updateInvoice.getCreateDate()).get());
        updatedInvoice.setTotal(updateInvoice.getTotal());
        updatedInvoice.setTaxCode(updateInvoice.getTaxcode());
        updatedInvoice.setOrder(invoiceOrder.get());

        invoiceRepository.updateInvoiceById(id, updatedInvoice);
        return res
            .setStatus(HttpStatus.OK)
            .setMessage("Invoice updated successfully!")
            .setData(updatedInvoice);
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
