package com.example.watch_selling.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.OrderDto;
import com.example.watch_selling.dtos.OrderUpdateDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.filters.CustomFilters;
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
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) return new ResponseDto<>(
            null,
            "Cannot find any order!",
            HttpStatus.NOT_FOUND.value()
        );

        return new ResponseDto<>(
            orders,
            "Orders found successfully!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<Order> findById(UUID id) {
        if (id.equals(null)) return new ResponseDto<>(
            null,
            "Invalid ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        Optional<Order> order = orderRepository.findById(id);
        if (!order.isPresent()) return new ResponseDto<>(
            null,
            "Cannot find order with the given ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseDto<>(
            order.get(),
            "Order found successfully!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<List<Order>> findByOrderDate(Date date) {
        if (date.equals(null)) return new ResponseDto<>(
            null,
            "Invalid date!",
            HttpStatus.BAD_REQUEST.value()
        );

        Optional<List<Order>> orders = orderRepository.findByOrderDate(date);
        if (!orders.isPresent()) return new ResponseDto<>(
            null,
            "Cannot find any order in the given date!",
            HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseDto<>(
            orders.get(),
            "Order found successfully!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<Order> createNewOrder(OrderDto newOrderDto) {
        if (newOrderDto.equals(null)) return new ResponseDto<>(
            null,
            "Invalid order!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (CustomFilters.idValidation(newOrderDto.getAccountId()) == false) return new ResponseDto<>(
            null,
            "Invalid account ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (CustomFilters.nameValidation(newOrderDto.getName()) == false) return new ResponseDto<>(
            null,
            "Invalid name!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (CustomFilters.addressValidation(newOrderDto.getAddress()) == false) return new ResponseDto<>(
            null,
            "Invalid name!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (CustomFilters.phoneNumberValidation(newOrderDto.getPhoneNumber()) == false) return new ResponseDto<>(
            null,
            "Invalid phone number!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (CustomFilters.dateValidation(newOrderDto.getOrderDate()) == false) return new ResponseDto<>(
            null,
            "Invalid order date!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (CustomFilters.dateValidation(newOrderDto.getDeliveryDate()) == false) return new ResponseDto<>(
            null,
            "Invalid delivery date!",
            HttpStatus.BAD_REQUEST.value()
        );

        List<String> statusList = Arrays.asList("Chờ duyệt", "Đang giao hàng", "Hoàn tất", "Đã huỷ");
        Boolean validStatus = false;
        for (String value: statusList) {
            if (newOrderDto.getStatus().equals(value)) {
                validStatus = true;
                break;
            }
        }
        if (!validStatus) return new ResponseDto<>(
            null,
            "Invalid status!",
            HttpStatus.BAD_REQUEST.value()
        );

        @SuppressWarnings("null")
        Optional<Account> associatedAccount = accountRepository.findById(newOrderDto.getAccountId());
        if (!associatedAccount.isPresent()) return new ResponseDto<>(
            null,
            "Cannot find the associated account with the given account ID! Invalid account ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        Optional<Order> newOrder = newOrderDto.toModel(false, associatedAccount.get());
        if (!newOrder.isPresent()) return new ResponseDto<>(
            null,
            "Error while converting DTO object to model of Order! May cause by invalid date format!",
            HttpStatus.BAD_REQUEST.value()
        );
        if (newOrder.get().getOrderDate().after(newOrder.get().getDeliveryDate())) return new ResponseDto<>(
            null,
            "Delivery date must comes after order date! Invalid order and delivery date!",
            HttpStatus.BAD_REQUEST.value()
        );
        
        Order order = orderRepository.save(newOrder.get());
        return new ResponseDto<>(
            order,
            "Orders found successfully!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<Order> updateOrderById(UUID id, OrderUpdateDto updateOrder) {
        if (id.equals(null)) return new ResponseDto<>(
            null,
            "Invalid ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        Optional<Order> targetingOrder = orderRepository.findById(id);
        if (!targetingOrder.isPresent()) return new ResponseDto<>(
            null,
            "Cannot find any order with the given ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (!CustomFilters.dateValidation(updateOrder.getOrderDate())) {
            return new ResponseDto<>(
                null,
                "Invalid order date!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        if (!CustomFilters.dateValidation(updateOrder.getDeliveryDate())) {
            return new ResponseDto<>(
                null,
                "Invalid delivery date!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        if (updateOrder.getAddress().isEmpty() || updateOrder.getAddress().isBlank()) {
            return new ResponseDto<>(
                null,
                "Invalid address!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        if (updateOrder.getName().isEmpty() || updateOrder.getName().isBlank()) {
            return new ResponseDto<>(
                null,
                "Invalid address!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        if (updateOrder.getPhoneNumber().isEmpty() || updateOrder.getPhoneNumber().isBlank()) {
            return new ResponseDto<>(
                null,
                "Invalid address!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        Order newOrder = targetingOrder.get();
        newOrder.setName(updateOrder.getName());
        newOrder.setAddress(updateOrder.getAddress());
        newOrder.setPhoneNumber(updateOrder.getPhoneNumber());
        try {
            newOrder.setOrderDate(new SimpleDateFormat("yyyy-MM-dd").parse(updateOrder.getOrderDate()));
            newOrder.setDeliveryDate(new SimpleDateFormat("yyyy-MM-dd").parse(updateOrder.getDeliveryDate()));
        }
        catch (ParseException e) {
            return new ResponseDto<>(
                null,
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value()
            );
        }
        orderRepository.updateById(id, newOrder);
        return new ResponseDto<>(
            newOrder,
            "Update order successfully!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<String> updateDeleteStatus(UUID id, Boolean status) {
        if (id.equals(null)) return new ResponseDto<>(
            null,
            "Invalid ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        Optional<Order> targetingOrder = orderRepository.findById(id);
        if (!targetingOrder.isPresent()) return new ResponseDto<>(
            null,
            "Cannot find any order with the given ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        orderRepository.updateDeleteStatus(id, status);
        return new ResponseDto<>(
            null,
            "Delete status update successfully!",
            HttpStatus.OK.value()
        );
    }
}
