package com.example.watch_selling.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.OrderDto;
import com.example.watch_selling.dtos.RequestDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Order;
import com.example.watch_selling.service.OrderService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("all")
    public ResponseEntity<ResponseDto<List<Order>>> getAllOrders() {
        ResponseDto<List<Order>> response = orderService.findAllOrders();
        if (response.getStatus().equals(HttpStatus.NOT_FOUND.value())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("new")
    public ResponseEntity<ResponseDto<Order>> createNewOrder(@RequestBody RequestDto<OrderDto> newOrder) {
        ResponseDto<Order> response = orderService.createNewOrder(newOrder.getData());
        if (response.getStatus().equals(HttpStatus.BAD_REQUEST.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
