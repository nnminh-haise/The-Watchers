package com.example.watch_selling.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.OrderDetailDto;
import com.example.watch_selling.dtos.RequestDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.CartDetail;
import com.example.watch_selling.model.OrderDetail;
import com.example.watch_selling.service.OrderDetailService;

@RestController
@RequestMapping(path = "api/order/detail")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("")
    public ResponseEntity<ResponseDto<OrderDetail>> readOrderDetailById(@RequestParam UUID id) {
        ResponseDto<OrderDetail> response = orderDetailService.findById(id);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("all")
    public ResponseEntity<ResponseDto<List<OrderDetail>>> readAllOrderDetail() {
        ResponseDto<List<OrderDetail>> response = orderDetailService.findAll();
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("new")
    public ResponseEntity<ResponseDto<OrderDetail>> createNewOrderDetail(@RequestBody RequestDto<OrderDetailDto> orderDetail) {
        ResponseDto<OrderDetail> response = orderDetailService.createNewOrderDetail(orderDetail.getData());
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // @PutMapping("new/from-cart")
    // public ResponseEntity<ResponseDto<List<OrderDetail>>> createOrderDetialsFromCartDetails(@RequestBody RequestDto<List<CartDetail>> cartDetails) {
    //     ResponseDto<List<OrderDetail>> response = orderDetailService.createNewOrderDetail(List<OrderDetail>orderDetail.getData());
    //     if (!response.getStatus().equals(HttpStatus.OK.value())) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
    //     }
    //     return ResponseEntity.status(HttpStatus.OK).body(response);
    // }

    @PatchMapping("update")
    public ResponseEntity<ResponseDto<OrderDetail>> updateOrderDetailById(
        @RequestParam("id") UUID id,
        @RequestBody RequestDto<OrderDetailDto> orderDetail
    ) {
        ResponseDto<OrderDetail> response = orderDetailService.updateOrderDetailById(id, orderDetail.getData());
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto<String>> deleteOrderDetail(@RequestParam("id") UUID id) {
        ResponseDto<String> response = orderDetailService.updateOrderDetailDeleteStatus(id, true);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
