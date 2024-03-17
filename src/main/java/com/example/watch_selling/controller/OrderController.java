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
import com.example.watch_selling.dtos.RequestDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.model.Order;
import com.example.watch_selling.service.OrderService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @SuppressWarnings("null")
    @GetMapping("my")
    public ResponseEntity<ResponseDto<List<Order>>> readAllOrders(
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

    @SuppressWarnings("null")
    @GetMapping("")
    public ResponseEntity<ResponseDto<Order>> getOrderById(@RequestParam UUID id) {
        ResponseDto<Order> res = orderService.findById(id);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @PutMapping("new")
    public ResponseEntity<ResponseDto<Order>> createNewOrder(@RequestBody RequestDto<CreateOrderDto> newOrder) {
        Account account = this.getCurrentAccount();
        ResponseDto<Order> res = orderService.createNewOrder(newOrder.getData(), account.getId());
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @PatchMapping("update")
    public ResponseEntity<ResponseDto<Order>> updateOrderById(
            @RequestParam UUID id,
            @RequestBody RequestDto<UpdateOrderDto> updateOrder) {
        ResponseDto<Order> res = orderService.updateOrderById(id, updateOrder.getData());
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto<String>> deleteOrderById(@RequestParam UUID id) {
        ResponseDto<String> res = orderService.updateDeleteStatus(id, true);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
