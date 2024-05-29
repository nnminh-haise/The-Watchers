package com.example.watch_selling.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.CreateOrderDto;
import com.example.watch_selling.dtos.UpdateOrderDto;
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

    public ResponseDto<List<Order>> findAllOrders(
            UUID accountId, int page, int size, String sortBy, String fromOrderDate, String toOrderDate) {
        ResponseDto<List<Order>> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (!sortBy.equalsIgnoreCase("asc") && !sortBy.equalsIgnoreCase("desc")) {
            return res.setMessage("Invalid sort by value!");
        }

        try {
            LocalDate validFromOrderDate = null;
            LocalDate validToOrderDate = null;
            if (fromOrderDate != null)
                validFromOrderDate = LocalDate.parse(fromOrderDate);
            if (toOrderDate != null)
                validToOrderDate = LocalDate.parse(toOrderDate);

            Pageable selectingPage = PageRequest.of(page, size);
            List<Order> orders = sortBy.equalsIgnoreCase("ASC")
                    ? orderRepository.findOrdersByAccountIdAndOrderDateASC(
                            accountId, validFromOrderDate, validToOrderDate, selectingPage)
                    : orderRepository.findOrdersByAccountIdAndOrderDateDESC(
                            accountId, validFromOrderDate, validToOrderDate, selectingPage);
            if (orders.isEmpty())
                return res
                        .setStatus(HttpStatus.NO_CONTENT)
                        .setMessage("Cannot find any order!");

            return res
                    .setMessage("Orders found successfully!")
                    .setStatus(HttpStatus.OK)
                    .setData(orders);
        } catch (Exception e) {
            return res.setMessage("Invalid order date!");
        }
    }

    public ResponseDto<Order> findById(UUID id) {
        ResponseDto<Order> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (id == null)
            return res.setMessage("Invalid ID!");

        Optional<Order> order = orderRepository.findById(id);
        if (!order.isPresent())
            return res.setMessage("Cannot find order with the given ID!");

        return res
                .setMessage("Order found successfully!")
                .setStatus(HttpStatus.OK)
                .setData(order.get());
    }

    public ResponseDto<Order> createNewOrder(CreateOrderDto dto, UUID accountId) {
        ResponseDto<Order> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        Optional<Account> associatedAccount = accountRepository.findById(accountId);
        if (!associatedAccount.isPresent()) {
            return res.setMessage("Cannot find the associated account with the given account ID!");
        }

        Optional<Order> order = CreateOrderDto.produce(dto, associatedAccount.get());
        if (order.isEmpty()) {
            return res.setMessage("Invalid DTO object!");
        }

        orderRepository.save(order.get());
        return res
                .setMessage("New order created successfully!")
                .setStatus(HttpStatus.OK)
                .setData(order.get());
    }

    public ResponseDto<Order> updateOrderById(UUID id, UpdateOrderDto dto) {
        ResponseDto<Order> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null) {
            return res.setMessage("Invalid order ID!");
        }

        Optional<Order> targetingOrder = orderRepository.findById(id);
        if (!targetingOrder.isPresent())
            return res.setMessage("Cannot find any order with the given ID!");

        Optional<Order> updatedOrder = UpdateOrderDto.produce(dto, targetingOrder.get());
        if (updatedOrder.isEmpty()) {
            return res.setMessage("Invalid DTO object!");
        }

        orderRepository.updateById(id, updatedOrder.get());
        return res
                .setMessage("Order updated successfully!")
                .setStatus(HttpStatus.OK)
                .setData(updatedOrder.get());
    }

    public ResponseDto<String> updateDeleteStatus(UUID id, Boolean status) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (id == null)
            return res.setMessage("Invalid ID!");

        Optional<Order> targetingOrder = orderRepository.findById(id);
        if (!targetingOrder.isPresent())
            return res.setMessage("Cannot find any order with the given ID!");

        orderRepository.updateDeleteStatus(id, status);
        return res
                .setMessage("Delete status update successfully!")
                .setStatus(HttpStatus.OK);
    }
}
