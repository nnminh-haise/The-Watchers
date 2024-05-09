package com.example.watch_selling.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.CreateInvoiceDto;
import com.example.watch_selling.dtos.InvoiceDetail;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.model.Invoice;
import com.example.watch_selling.service.InvoiceService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/invoice")
public class InvoiceController {
        @Autowired
        private InvoiceService invoiceService;

        private Account getCurrentAccount() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                return (Account) authentication.getPrincipal();
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Request success!"),
                        @ApiResponse(responseCode = "400", description = "Bad request!"),
                        @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
        })
        @SuppressWarnings("null")
        @GetMapping("my")
        public ResponseEntity<ResponseDto<List<InvoiceDetail>>> readAllInvoiceByAccountId(
                        @RequestParam(name = "page", defaultValue = "0") Integer page,
                        @RequestParam(name = "size", defaultValue = "10") Integer size,
                        @RequestParam(name = "date_sort_by", defaultValue = "asc") String dateSortBy,
                        @RequestParam(name = "from_date", required = false) String fromDate,
                        @RequestParam(name = "to_date", required = false) String toDate,
                        @RequestParam(name = "total_sort_by", defaultValue = "asc") String totalSortBy,
                        @RequestParam(name = "from_total", required = false) String fromTotal,
                        @RequestParam(name = "to_total", required = false) String toTotal) {
                Account owner = getCurrentAccount();
                ResponseDto<List<InvoiceDetail>> res = invoiceService.findAllInvoiceByAccountId(
                                page, size, dateSortBy, fromDate, toDate, totalSortBy, fromTotal, toTotal,
                                owner.getId());
                return ResponseEntity.status(res.getStatus()).body(res);
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Request success!"),
                        @ApiResponse(responseCode = "400", description = "Bad request!"),
                        @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
        })
        @SuppressWarnings("null")
        @GetMapping("/{id}")
        public ResponseEntity<ResponseDto<InvoiceDetail>> readInvoiceById(@PathVariable UUID id) {
                ResponseDto<InvoiceDetail> res = invoiceService.findById(id);
                return ResponseEntity.status(res.getStatus()).body(res);
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Request success!"),
                        @ApiResponse(responseCode = "400", description = "Bad request!"),
                        @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
        })
        @SuppressWarnings("null")
        @PostMapping("new")
        public ResponseEntity<ResponseDto<InvoiceDetail>> createInvoice(
                        @RequestBody CreateInvoiceDto dto) {
                ResponseDto<InvoiceDetail> res = invoiceService.createNewInvoice(dto);
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
