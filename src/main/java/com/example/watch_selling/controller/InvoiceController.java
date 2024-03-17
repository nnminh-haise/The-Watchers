package com.example.watch_selling.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.InvoiceUpdateDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Invoice;
import com.example.watch_selling.service.InvoiceService;

@RestController
@RequestMapping("api/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @SuppressWarnings("null")
    @GetMapping("all")
    public ResponseEntity<ResponseDto<List<Invoice>>> readAllInvoices() {
        ResponseDto<List<Invoice>> res = invoiceService.findAllInvoices();
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @GetMapping("")
    public ResponseEntity<ResponseDto<Invoice>> readInvoiceById(@RequestParam UUID id) {
        ResponseDto<Invoice> res = invoiceService.findById(id);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @PutMapping("new")
    public ResponseEntity<ResponseDto<Invoice>> createInvoice(@RequestBody InvoiceUpdateDto newInvoice) {
        ResponseDto<Invoice> res = invoiceService.createNewInvoice(newInvoice);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @PatchMapping("update")
    public ResponseEntity<ResponseDto<Invoice>> updateInvoiceById(
            @RequestParam UUID id,
            @RequestBody InvoiceUpdateDto updateInvoice) {
        ResponseDto<Invoice> res = invoiceService.updateInvoiceById(id, updateInvoice);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto<String>> deleteInvoiceById(@RequestParam UUID id) {
        ResponseDto<String> res = invoiceService.updateDeleteStatusById(id, true);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
