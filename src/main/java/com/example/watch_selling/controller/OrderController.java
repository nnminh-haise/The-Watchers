package com.example.watch_selling.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.CreateOrderDto;
import com.example.watch_selling.dtos.UpdateOrderDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.model.Order;
import com.example.watch_selling.service.OrderService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

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
    @GetMapping("all")
    public ResponseEntity<ResponseDto<List<Order>>> findAllOrderWithFilterAndSorting(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sort_by", defaultValue = "asc") String sortBy,
            @RequestParam(name = "from", required = false) String fromOrderDate,
            @RequestParam(name = "to", required = false) String toOrderDate) {
        Account account = this.getCurrentAccount();

        ResponseDto<List<Order>> res = orderService.findAllOrders(
                account.getId(), page, size, sortBy, fromOrderDate, toOrderDate);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request success!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @SuppressWarnings("null")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Order>> findOrderById(@PathVariable UUID id) {
        ResponseDto<Order> res = orderService.findById(id);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request success!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @SuppressWarnings("null")
    @PostMapping("new")
    public ResponseEntity<ResponseDto<Order>> createOrder(@RequestBody CreateOrderDto dto) {
        Account account = this.getCurrentAccount();

        ResponseDto<Order> res = orderService.createNewOrder(dto, account.getId());
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request success!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @SuppressWarnings("null")
    @PutMapping("update/{id}")
    public ResponseEntity<ResponseDto<Order>> updateOrderById(
            @PathVariable UUID id,
            @RequestBody UpdateOrderDto dto) {
        ResponseDto<Order> res = orderService.updateOrderById(id, dto);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request success!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @SuppressWarnings("null")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ResponseDto<String>> deleteOrderById(@PathVariable UUID id) {
        ResponseDto<String> res = orderService.updateDeleteStatus(id, true);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
