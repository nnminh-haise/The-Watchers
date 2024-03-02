package com.example.watch_selling.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.OrderDto;
import com.example.watch_selling.dtos.OrderUpdateDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.model.Order;
import com.example.watch_selling.repository.AccountRepository;
import com.example.watch_selling.repository.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    public OrderService(
        OrderRepository orderRepository,
        AccountRepository accountRepository
    ) {
        this.orderRepository = orderRepository;
        this.accountRepository = accountRepository;
    }

    public ResponseDto<List<Order>> findAllOrders() {
        ResponseDto<List<Order>> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) return res.setMessage("Cannot find any order!");

        return res
            .setMessage("Orders found successfully!")
            .setStatus(HttpStatus.OK)
            .setData(orders);
    }

    public ResponseDto<Order> findById(UUID id) {
        ResponseDto<Order> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (id == null) return res.setMessage("Invalid ID!");

        Optional<Order> order = orderRepository.findById(id);
        if (!order.isPresent()) return res.setMessage("Cannot find order with the given ID!");

        return res
            .setMessage("Order found successfully!")
            .setStatus(HttpStatus.OK)
            .setData(order.get());
    }

    public ResponseDto<Order> createNewOrder(OrderDto newOrderDto) {
        ResponseDto<Order> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (newOrderDto == null) return res.setMessage("Invalid order!");

        ResponseDto<String> dtoValidationResponse = OrderDto.validDto(newOrderDto);
        if (!dtoValidationResponse.getStatus().equals(HttpStatus.OK)) {
            return res
                .setMessage(dtoValidationResponse.getMessage())
                .setStatus(dtoValidationResponse.getStatus());
        }

        if (newOrderDto.getAccountID() == null) {
            return res.setMessage("Invalid account ID!");
        }
        @SuppressWarnings("null")
        Optional<Account> associatedAccount = accountRepository.findById(newOrderDto.getAccountID());
        if (!associatedAccount.isPresent()) {
            return res.setMessage("Cannot find the associated account with the given account ID! Invalid account ID!");
        }
        
        Order order = orderRepository.save(
            OrderDto.toModel(newOrderDto, false, associatedAccount.get()).get()
        );
        return res
            .setMessage("New order created successfully!")
            .setStatus(HttpStatus.OK)
            .setData(order);
    }

    public ResponseDto<Order> updateOrderById(UUID id, OrderUpdateDto updateOrder) {
        ResponseDto<Order> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null) return res.setMessage("Invalid ID!");

        Optional<Order> targetingOrder = orderRepository.findById(id);
        if (!targetingOrder.isPresent()) return res.setMessage("Cannot find any order with the given ID!");

        ResponseDto<String> dtoValidatResponse = OrderUpdateDto.validDto(updateOrder);
        if (!dtoValidatResponse.getStatus().equals(HttpStatus.OK)) {
            return res.setMessage(dtoValidatResponse.getMessage());
        }

        Order newOrder = targetingOrder.get();
        newOrder.setName(updateOrder.getName());
        newOrder.setAddress(updateOrder.getAddress());
        newOrder.setPhoneNumber(updateOrder.getPhoneNumber());
        newOrder.setOrderDate(OrderDto.parseDate(updateOrder.getOrderDate()).get());
        newOrder.setDeliveryDate(OrderDto.parseDate(updateOrder.getDeliveryDate()).get());

        orderRepository.updateById(id, newOrder);
        return res
            .setMessage("Order updated successfully!")
            .setStatus(HttpStatus.OK)
            .setData(newOrder);
    }

    public ResponseDto<String> updateDeleteStatus(UUID id, Boolean status) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (id == null) return res.setMessage("Invalid ID!");

        Optional<Order> targetingOrder = orderRepository.findById(id);
        if (!targetingOrder.isPresent()) return res.setMessage("Cannot find any order with the given ID!");

        orderRepository.updateDeleteStatus(id, status);
        return res
            .setMessage("Delete status update successfully!")
            .setStatus(HttpStatus.OK);
    }
}
