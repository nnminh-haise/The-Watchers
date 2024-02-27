package com.example.watch_selling.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.OrderDto;
import com.example.watch_selling.dtos.OrderUpdateDto;
import com.example.watch_selling.dtos.RequestDto;
import com.example.watch_selling.dtos.ResponseDto;
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

    @GetMapping("all")
    public ResponseEntity<ResponseDto<List<Order>>> getAllOrders() {
        ResponseDto<List<Order>> response = orderService.findAllOrders();
        if (response.getStatus().equals(HttpStatus.NOT_FOUND.value())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("")
    public ResponseEntity<ResponseDto<Order>> getOrderById(@RequestParam UUID id) {
        ResponseDto<Order> response = orderService.findById(id);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("search")
    public ResponseEntity<ResponseDto<List<Order>>> getOrderByOrderDate(@RequestParam String date) {
        Date validDate;
        try {
            validDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        }
        catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto<>(
                null,
                "Invalid date! Date must follow the format YYYY-MM-DD!",
                HttpStatus.BAD_REQUEST.value()
            ));
        }
        ResponseDto<List<Order>> response = orderService.findByOrderDate(validDate);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
    
    @PatchMapping("update")
    public ResponseEntity<ResponseDto<Order>> updateOrderById(
        @RequestParam UUID id,
        @RequestBody RequestDto<OrderUpdateDto> updateOrder
    ) {
        ResponseDto<Order> response = orderService.updateOrderById(id, updateOrder.getData());
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto<String>> deleteOrderById(@RequestParam UUID id) {
        ResponseDto<String> response = orderService.updateDeleteStatus(id, true);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
