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

        System.out.println("Log: " + accountId);

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

    public ResponseDto<Order> createNewOrder(CreateOrderDto newOrderDto, UUID accountId) {
        ResponseDto<Order> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (newOrderDto == null)
            return res.setMessage("Invalid order!");

        ResponseDto<String> dtoValidationResponse = CreateOrderDto.validDto(newOrderDto);
        if (!dtoValidationResponse.getStatus().equals(HttpStatus.OK)) {
            return res
                    .setMessage(dtoValidationResponse.getMessage())
                    .setStatus(dtoValidationResponse.getStatus());
        }

        @SuppressWarnings("null")
        Optional<Account> associatedAccount = accountRepository.findById(accountId);
        if (!associatedAccount.isPresent()) {
            return res.setMessage("Cannot find the associated account with the given account ID! Invalid account ID!");
        }

        Order order = orderRepository.save(
                CreateOrderDto.toModel(newOrderDto, "Chờ duyệt", false, associatedAccount.get()).get());
        return res
                .setMessage("New order created successfully!")
                .setStatus(HttpStatus.OK)
                .setData(order);
    }

    public ResponseDto<Order> updateOrderById(UUID id, UpdateOrderDto updateOrder) {
        ResponseDto<Order> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null)
            return res.setMessage("Invalid ID!");

        Optional<Order> targetingOrder = orderRepository.findById(id);
        if (!targetingOrder.isPresent())
            return res.setMessage("Cannot find any order with the given ID!");

        ResponseDto<String> dtoValidataResponse = UpdateOrderDto.validDto(updateOrder);
        if (!dtoValidataResponse.getStatus().equals(HttpStatus.OK)) {
            return res.setMessage(dtoValidataResponse.getMessage());
        }

        Order newOrder = targetingOrder.get();
        newOrder.setName(updateOrder.getName());
        newOrder.setAddress(updateOrder.getAddress());
        newOrder.setPhoneNumber(updateOrder.getPhoneNumber());
        newOrder.setOrderDate(CreateOrderDto.parseDate(updateOrder.getOrderDate()).get());
        newOrder.setDeliveryDate(CreateOrderDto.parseDate(updateOrder.getDeliveryDate()).get());

        orderRepository.updateById(id, newOrder);
        return res
                .setMessage("Order updated successfully!")
                .setStatus(HttpStatus.OK)
                .setData(newOrder);
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
