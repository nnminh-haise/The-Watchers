package com.example.watch_selling.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.CreateInvoiceDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Invoice;
import com.example.watch_selling.service.InvoiceService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request success!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @SuppressWarnings("null")
    @GetMapping("all")
    public ResponseEntity<ResponseDto<List<Invoice>>> readAllInvoices(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "date_sort_by", defaultValue = "asc") String dateSortBy,
            @RequestParam(name = "from_date", required = false) String fromDate,
            @RequestParam(name = "to_date", required = false) String toDate,
            @RequestParam(name = "total_sort_by", defaultValue = "asc") String totalSortBy,
            @RequestParam(name = "from_total", required = false) String fromTotal,
            @RequestParam(name = "to_total", required = false) String toTotal) {
        ResponseDto<List<Invoice>> res = invoiceService.findAllInvoices(
                page, size, dateSortBy, fromDate, toDate, totalSortBy, fromTotal, toTotal);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request success!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @SuppressWarnings("null")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Invoice>> readInvoiceById(@PathVariable UUID id) {
        ResponseDto<Invoice> res = invoiceService.findById(id);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request success!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @SuppressWarnings("null")
    @PostMapping("new")
    public ResponseEntity<ResponseDto<Invoice>> createInvoice(
            @RequestBody CreateInvoiceDto dto) {
        ResponseDto<Invoice> res = invoiceService.createNewInvoice(dto);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request success!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @SuppressWarnings("null")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ResponseDto<String>> deleteInvoiceById(@PathVariable UUID id) {
        ResponseDto<String> res = invoiceService.updateDeleteStatusById(id, true);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
