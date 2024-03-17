package com.example.watch_selling.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.CreateOrderDetailDto;
import com.example.watch_selling.dtos.RequestDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.dtos.UpdateCartDetailDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.model.OrderDetail;
import com.example.watch_selling.service.OrderDetailService;

@RestController
@RequestMapping(path = "api/order/detail")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;

    private Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Account) authentication.getPrincipal();
    }

    @SuppressWarnings("null")
    @GetMapping("")
    public ResponseEntity<ResponseDto<OrderDetail>> readOrderDetailById(@RequestParam UUID id) {
        ResponseDto<OrderDetail> res = orderDetailService.findById(id);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @GetMapping("all-of")
    public ResponseEntity<ResponseDto<List<OrderDetail>>> readAllOrderDetailOfOrder(
            @RequestParam("order_id") UUID orderId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sort_by", defaultValue = "asc") String sortBy) {
        ResponseDto<List<OrderDetail>> res = orderDetailService.findAllOrderDetailByOrderId(
                orderId, page, size, sortBy);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    // @GetMapping("all")
    // public ResponseEntity<ResponseDto<List<OrderDetail>>>
    // readAllOrdersIncludeOrderDetailsOfAccount(
    // @RequestParam(name = "page", defaultValue = "0") Integer page,
    // @RequestParam(name = "size", defaultValue = "10") Integer size,
    // @RequestParam(name = "sort_by", defaultValue = "asc") String sortBy,
    // @RequestParam(name = "from", required = false) String fromOrderDate,
    // @RequestParam(name = "to", required = false) String toOrderDate) {
    // Account account = this.getCurrentAccount();
    // ResponseDto<List<OrderDetail>> res = orderDetailService
    // .findAllOrdersIncludeOrderDetailsOfAccount(account.getId());
    // return ResponseEntity.status(res.getStatus()).body(res);
    // }

    @SuppressWarnings("null")
    @PutMapping("new")
    public ResponseEntity<ResponseDto<OrderDetail>> createNewOrderDetail(
            @RequestBody RequestDto<CreateOrderDetailDto> orderDetail) {
        ResponseDto<OrderDetail> res = orderDetailService.createNewOrderDetail(orderDetail.getData());
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @PutMapping("new-from")
    public ResponseEntity<ResponseDto<List<OrderDetail>>> createOrderDetailsFromCartDetails(
            @RequestParam("order_id") UUID orderId,
            @RequestBody RequestDto<List<UUID>> cartDetailIds) {
        ResponseDto<List<OrderDetail>> res = orderDetailService
                .createOrderDetailsFromCartDetailsOfOrder(orderId, cartDetailIds.getData());
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @PatchMapping("update")
    public ResponseEntity<ResponseDto<OrderDetail>> updateOrderDetailById(
            @RequestParam("id") UUID id,
            @RequestBody RequestDto<UpdateCartDetailDto> orderDetail) {
        ResponseDto<OrderDetail> res = orderDetailService.updateOrderDetailById(id, orderDetail.getData());
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto<String>> deleteOrderDetail(@RequestParam("id") UUID id) {
        ResponseDto<String> res = orderDetailService.updateOrderDetailDeleteStatus(id, true);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
